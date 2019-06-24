package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping(path = "/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;


    @PostMapping(path = "/products/add", consumes = "application/json")
    public ResponseEntity<? extends  Object> createProduct(@RequestBody Map<String, Object> productJson) {
        System.out.println(productJson);
        Product product = new Product();
        product.setName(productJson.get("name").toString());
        product.setDescription(productJson.get("description").toString());
        product.setPrice(Double.parseDouble(productJson.get("price").toString()));
        product.setQuantity(Double.parseDouble(productJson.get("quantity").toString()));
        if (productJson.containsKey("category"))
            product.setCategory(categoryRepository.getOne(productJson.get("category").toString()));
        System.out.println(product);
        return new ResponseEntity<>(productRepository.save(product), HttpStatus.CREATED);
    }

}
