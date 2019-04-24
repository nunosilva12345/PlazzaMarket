/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.tqs.g6.project.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.tqs.g6.project.Entities.Category;
import ua.tqs.g6.project.Entities.Product;
import ua.tqs.g6.project.Repositories.CategoryRepository;
import ua.tqs.g6.project.Repositories.ProductRepository;

@Controller
@RequestMapping(path = "/product")
public class ProductController {
    
    @Autowired
    private ProductRepository productRepo;
    
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<Product> findAll() {
        return productRepo.findAll();
    }

    @GetMapping(path = "/{id}")
    public Product find(@PathVariable("id") int id) {
        Product product = null;
        product = productRepo.getOne(id);
        return product;
       
    }

    @PostMapping(consumes = "application/json")
    public Product create(@RequestBody Product product) {
        return productRepo.save(product);
    }
    
}
