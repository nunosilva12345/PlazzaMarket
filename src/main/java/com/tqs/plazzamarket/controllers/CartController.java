package com.tqs.plazzamarket.controllers;

// import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.utils.Cart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/cart")
public class CartController {
    @Autowired
    private ProductRepository productRepository;

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
}