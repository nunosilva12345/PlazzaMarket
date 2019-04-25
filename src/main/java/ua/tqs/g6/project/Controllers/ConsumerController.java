/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.tqs.g6.project.Controllers;

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
import ua.tqs.g6.project.Entities.Consumer;
import ua.tqs.g6.project.Repositories.ConsumerRepository;

@Controller
@RequestMapping(path = "/consumer")
@Api(value="Consumer", description="Operations with entity consumer")
public class ConsumerController {
    
    @Autowired
    private ConsumerRepository consumerRepo;
    
    @ApiOperation(value = "Get all consumers")
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<Consumer> findAll() {
        return consumerRepo.findAll();
    }
    
    @ApiOperation(value = "Get consumer by id")
    @GetMapping(path = "/{id}")
    public Consumer find(@PathVariable("id") int id) {
        Consumer consumer = null;
        consumer = consumerRepo.getOne(id);
        return consumer;
       
    }
    
    @ApiOperation(value = "Add consumer to database")
    @PostMapping(consumes = "application/json")
    public Consumer create(@RequestBody Consumer consumer) {
        return consumerRepo.save(consumer);
    }
}
