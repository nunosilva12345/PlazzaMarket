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
import ua.tqs.g6.project.Entities.Producer;
import ua.tqs.g6.project.Repositories.CategoryRepository;
import ua.tqs.g6.project.Repositories.ProducerRepository;

@Controller
@RequestMapping(path = "/producer")
public class ProducerController {
    
    @Autowired
    private ProducerRepository producerRepo;
    
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<Producer> findAll() {
        return producerRepo.findAll();
    }

    @GetMapping(path = "/{id}")
    public Producer find(@PathVariable("id") int id) {
        Producer producer = null;
        producer = producerRepo.getOne(id);
        return producer;
       
    }

    @PostMapping(consumes = "application/json")
    public Producer create(@RequestBody Producer producer) {
        return producerRepo.save(producer);
    }
    
}
