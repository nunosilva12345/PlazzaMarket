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
import ua.tqs.g6.project.Entities.ProductName;
import ua.tqs.g6.project.Repositories.CategoryRepository;
import ua.tqs.g6.project.Repositories.ProductNameRepository;

@Controller
@RequestMapping(path = "/productname")
public class ProductNameController {
    @Autowired
    private ProductNameRepository productNameRepo;
    
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<ProductName> findAll() {
        return productNameRepo.findAll();
    }

    @GetMapping(path = "/{id}")
    public ProductName find(@PathVariable("id") int id) {
        ProductName productName = null;
        productName = productNameRepo.getOne(id);
        return productName;
       
    }

    @PostMapping(consumes = "application/json")
    public ProductName create(@RequestBody ProductName productName) {
        return productNameRepo.save(productName);
    }
}
