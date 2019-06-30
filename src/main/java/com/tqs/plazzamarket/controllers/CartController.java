package com.tqs.plazzamarket.controllers;

import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.entities.Sale;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.repositories.SaleRepository;
import com.tqs.plazzamarket.utils.Cart;

import com.tqs.plazzamarket.utils.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/cart")
@Api(tags = { "Cart" })
@SwaggerDefinition(tags = {
    @Tag(name = "Cart", description = "Operations with cart")
})
public class CartController {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @ApiOperation(value = "Add product to cart")
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

    @ApiOperation(value = "Clear cart")
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

    @ApiOperation(value = "Remove product by id")
    @DeleteMapping(path = "/remove/{id}")
    public ResponseEntity<Integer> removeProduct(@ApiParam("product id") @PathVariable("id") int id, HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("cart") == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Cart cart = (Cart) session.getAttribute("cart");
        if (cart.removeProduct(id)){
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Confirm shopping cart")
    @PostMapping(path = "/confirm")
    @Transactional
    public ResponseEntity<Integer> confirmBuy(HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("cart") == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Cart cart = (Cart) session.getAttribute("cart");
        Map<Integer, Double[]> items = cart.getItems();

        for (Map.Entry<Integer,Double[]> entry : items.entrySet()) {
            int key = entry.getKey();
            Double[] pair = entry.getValue();
            Sale sale = new Sale();
            Product product = productRepository.getOne(key);
            product.subtractQuantity(pair[0]);
            productRepository.saveAndFlush(product);
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