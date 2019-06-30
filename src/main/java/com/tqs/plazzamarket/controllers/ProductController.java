package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Admin;
import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api")
@Api(value="Product", description="Operations with products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Transactional
    @ApiOperation(value = "Create Product", response = Product.class)
    @PostMapping(path = "/products/add", consumes = "application/json")
    public ResponseEntity<Product> createProduct(@RequestBody Map<String, Object> productJson, HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("user").getClass() != Producer.class)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Product product = new Product();
        product.setName(productJson.get("name").toString());
        product.setDescription(productJson.get("description").toString());
        product.setPrice(Double.parseDouble(productJson.get("price").toString()));
        product.setQuantity(Double.parseDouble(productJson.get("quantity").toString()));
        if (productJson.containsKey("category"))
            product.setCategory(categoryRepository.getOne(productJson.get("category").toString()));
        Producer producer = (Producer) session.getAttribute("user");
        if (producer != null)
            product.setProducer(producerRepository.getOne(producer.getUsername()));
        return new ResponseEntity<>(productRepository.saveAndFlush(product), HttpStatus.CREATED);
    }

    @ApiOperation(value = "List all products", response = List.class)
    @GetMapping(path = "/products")
    public @ResponseBody Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @ApiOperation(value = "Remove product by id")
    @DeleteMapping(path = "/products/remove/{id}")
    public ResponseEntity<Object> removeProduct(@ApiParam("product id") @PathVariable("id") int id, HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("user") == Consumer.class)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Optional<Product> product = productRepository.findById(id); 
        if (!product.isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (session.getAttribute("user").getClass() != Admin.class && !product.get().getProducer().getUsername().equals(((Producer) session.getAttribute("user")).getUsername()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        productRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "List of user's products", response = List.class)
    @GetMapping(path = "/products/{username}")
    public ResponseEntity<List<Product>> listProducerProducts(@ApiParam("username") @PathVariable("username") String username) {
        Optional<Producer> producer = producerRepository.findById(username);
        if (producer.isPresent())
            return new ResponseEntity<>(producer.get().getProducts(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "List of category's products", response = List.class)
    @GetMapping(path = "/products/category/{category}")
    public ResponseEntity<List<Product>> searchProductsCategory(@ApiParam("category name") @PathVariable("category") String category) {
        Optional<Category> optional = categoryRepository.findById(category);
        if (!optional.isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(optional.get().getProducts(), HttpStatus.OK);
    }
}
