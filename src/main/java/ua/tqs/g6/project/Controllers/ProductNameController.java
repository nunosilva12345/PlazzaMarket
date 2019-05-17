
package ua.tqs.g6.project.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.tqs.g6.project.entities.ProductName;
import ua.tqs.g6.project.repositories.ProductNameRepository;

@Controller
@RequestMapping(path = "/productname")
@Api(value="Product", description="Operations with entity ProductName")
public class ProductNameController {
    @Autowired
    private ProductNameRepository productNameRepo;
    
    @ApiOperation(value = "Get all product names")
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<ProductName> findAll() {
        return productNameRepo.findAll();
    }
    
    @ApiOperation(value = "Get products by id")
    @GetMapping(path = "/{id}")
    public ProductName find(@PathVariable("id") int id) {
        ProductName productName = null;
        productName = productNameRepo.getOne(id);
        return productName;
       
    }

    @ApiOperation(value = "Add product name to database")
    @PostMapping(consumes = "application/json")
    public ProductName create(@RequestBody ProductName productName) {
        return productNameRepo.save(productName);
    }
}
