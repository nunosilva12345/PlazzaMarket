package com.tqs.plazzamarket.controllers;


import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
public class CategoryController {

    @Autowired
    private CategoryRepository categoryRepository;


    @RequestMapping(path = "/category/init", method = RequestMethod.GET)
    public void init() {
        System.out.println("Aqui");
        Category c = new Category("Flowers");
        categoryRepository.save(c);
    }

}
