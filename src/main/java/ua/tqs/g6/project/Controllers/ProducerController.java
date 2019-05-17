
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
import ua.tqs.g6.project.entities.Category;
import ua.tqs.g6.project.entities.Producer;
import ua.tqs.g6.project.repositories.CategoryRepository;
import ua.tqs.g6.project.repositories.ProducerRepository;

@Controller
@RequestMapping(path = "/producer")
@Api(value="Producer", description="Operations with entity producer")
public class ProducerController {
    
    @Autowired
    private ProducerRepository producerRepo;
    
    @ApiOperation(value = "Get all procucers")
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<Producer> findAll() {
        return producerRepo.findAll();
    }
    
    @ApiOperation(value = "Get producer by id")
    @GetMapping(path = "/{id}")
    public Producer find(@PathVariable("id") int id) {
        Producer producer = null;
        producer = producerRepo.getOne(id);
        return producer;
       
    }
    
    @ApiOperation(value = "Add producer to database")
    @PostMapping(consumes = "application/json")
    public Producer create(@RequestBody Producer producer) {
        return producerRepo.save(producer);
    }
    
}
