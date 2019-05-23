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
import ua.tqs.g6.project.entities.Sales;
import ua.tqs.g6.project.repositories.SalesRepository;

@Controller
@RequestMapping(path = "/sales")
@Api(value="Sale", description="Operations with entity sale")
public class SalesController {
    @Autowired
    private SalesRepository salesRepo;
    
    @ApiOperation(value = "Get all sales")
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<Sales> findAll() {
        return salesRepo.findAll();
    }
    
    @ApiOperation(value = "Get sale by id")
    @GetMapping(path = "/{id}")
    public Sales find(@PathVariable("id") int id) {
        Sales sales = null;
        sales = salesRepo.getOne(id);
        return sales;
       
    }
    
    @ApiOperation(value = "Add sale to database")
    @PostMapping(consumes = "application/json")
    public Sales create(@RequestBody Sales sales) {
        return salesRepo.save(sales);
    }
    
}
