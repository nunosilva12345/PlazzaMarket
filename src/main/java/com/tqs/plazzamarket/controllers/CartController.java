package com.tqs.plazzamarket.controllers;

// import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.servlet.http.HttpSession;

import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.entities.Sale;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.repositories.SaleRepository;
import com.tqs.plazzamarket.utils.Cart;

import com.tqs.plazzamarket.utils.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/cart")
public class CartController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @PostMapping(path = "/add", consumes = "application/json")
    public ResponseEntity<Double> add(@RequestBody Map<String, Object> content, HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("cart") == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Number productId = (Number) content.get("productId");
        Number quantity = (Number) content.get("quantity");
        if (productId == null || quantity == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Optional<Product> product = productRepository.findById(productId.intValue());
        if (!product.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Cart cart = (Cart) session.getAttribute("cart");
        quantity = cart.add(product.get(), quantity.doubleValue());
        if (quantity == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(quantity.doubleValue(), HttpStatus.CREATED);
    }
    
    @DeleteMapping(path = "/clear")
    public ResponseEntity<Integer> clearList(HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("cart") == null) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Cart cart = (Cart) session.getAttribute("cart");
        cart.clearList();
        int size = cart.size();
        if (size != 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(size, HttpStatus.OK);
    }

    @DeleteMapping(path = "/remove/{id}")
    public ResponseEntity<Integer> removeProduct(@PathVariable("id") int id, HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("cart") == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart.removeProduct(id)){
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/confirm")
    public ResponseEntity<Integer> confirmBuy(HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("cart") == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Cart cart = (Cart) session.getAttribute("cart");
        Map<Integer, Double[]> items = cart.getItems();
        for(int key : items.keySet()) {
            Double[] pair = items.get(key);
            Sale sale = new Sale();
            sale.setProduct(productRepository.getOne(key));
            sale.setConsumer((Consumer) session.getAttribute("user"));
            sale.setQuantity(pair[0]);
            sale.setStatus(Status.PROCESSING);
            saleRepository.saveAndFlush(sale);
        }
        session.setAttribute("cart", new Cart());
        return new ResponseEntity<>(HttpStatus.OK);
    }
}