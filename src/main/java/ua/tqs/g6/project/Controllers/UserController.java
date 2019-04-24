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
import ua.tqs.g6.project.Entities.Sales;
import ua.tqs.g6.project.Entities.User;
import ua.tqs.g6.project.Repositories.SalesRepository;
import ua.tqs.g6.project.Repositories.UserRepository;

@Controller
@RequestMapping(path = "/user")
public class UserController {
    @Autowired
    private UserRepository userRepo;
    
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<User> findAll() {
        return userRepo.findAll();
    }

    @GetMapping(path = "/{id}")
    public User find(@PathVariable("id") int id) {
        User user = null;
        user = userRepo.getOne(id);
        return user;
       
    }

    @PostMapping(consumes = "application/json")
    public User create(@RequestBody User user) {
        return userRepo.save(user);
    }
}
