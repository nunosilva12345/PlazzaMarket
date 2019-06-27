package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @PostMapping(path = "/products/add", consumes = "application/json")
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> productJson, HttpSession httpSession) {
        Product product = new Product();
        product.setName(productJson.get("name").toString());
        product.setDescription(productJson.get("description").toString());
        product.setPrice(Double.parseDouble(productJson.get("price").toString()));
        product.setQuantity(Double.parseDouble(productJson.get("quantity").toString()));
        if (productJson.containsKey("category"))
            product.setCategory(categoryRepository.getOne(productJson.get("category").toString()));
        Producer producer = (Producer) httpSession.getAttribute("user");
        if (producer != null)
            product.setProducer(producer);
        return new ResponseEntity<>(productRepository.saveAndFlush(product), HttpStatus.CREATED);
    }

    @GetMapping(path = "/products/")
    public @ResponseBody Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @GetMapping(path = "/products/remove/{id}")
    public ResponseEntity<Object> removeProduct(@PathVariable("id") int id) {
        Optional<Product> product = productRepository.findById(id);
        if (!product.isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        productRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/products/{username}")
    public ResponseEntity<List<Product>> listProducerProducts(@PathVariable("username") String username) {
        Optional<Producer> producer = producerRepository.findById(username);
        if (producer.isPresent()) {
            return new ResponseEntity<>(producer.get().getProducts(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping(path = "/products/category/{category}")
    public ResponseEntity<List<Product>> searchProductsCategory(@PathVariable("category") String category) {
        Optional<Category> categoria = categoryRepository.findById(category);
        List<Product> listProductCategory = categoria.get().getProducts();
        if (!categoria.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
                
        return new ResponseEntity<List<Product>>(listProductCategory,HttpStatus.OK);
    }

}
