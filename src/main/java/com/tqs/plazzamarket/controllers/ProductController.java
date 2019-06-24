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
        Category category = new Category("Flowers");
        categoryRepository.saveAndFlush(category);
        Product product = new Product();
        product.setName(productJson.get("name").toString());
        product.setDescription(productJson.get("description").toString());
        product.setPrice(Double.parseDouble(productJson.get("price").toString()));
        product.setQuantity(Double.parseDouble(productJson.get("quantity").toString()));
        product.setCategory(categoryRepository.getOne(productJson.get("category").toString()));
        return new ResponseEntity<Product>(productRepository.saveAndFlush(product), HttpStatus.CREATED);
    }

    @GetMapping(path = "/products/")
    public @ResponseBody Iterable<Product> findAll() {
        return productRepository.findAll();
    }
}
