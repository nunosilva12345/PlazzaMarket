package com.tqs.plazzamarket.controllers;


import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping(path = "/api")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    
    //new
    @Autowired
    private ProducerRepository producerRepository;


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

    @GetMapping(path = "/products/")
    public @ResponseBody Iterable<Product> findAll() {
        return productRepository.findAll();
    }
    
    @GetMapping(path = "/products/remove/{id}")
    public ResponseEntity<? extends Object> removeProduct(@PathVariable("id") int id) {
        System.out.println(productRepository.findAll());
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            productRepository.deleteById(id);
            return new ResponseEntity<Product>(HttpStatus.OK);
        }
        return new ResponseEntity<Product>(HttpStatus.BAD_REQUEST);
    }
    
    @GetMapping(path = "/products/{producer_id}")
    public List<Product> listProducerProducts(@PathVariable("producer_id") String producer_id) {
        Optional<Producer> producer = producerRepository.findById(producer_id);
        
        if(!producer.isPresent()){
            return null;
        }
        return producer.get().getProducts();
    }
}
