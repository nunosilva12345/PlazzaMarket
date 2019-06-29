package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
@Api(value="Category", description="Operations with categories")
public class CategoryController {
    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping(path = "/category/init")
    public void init() {
        String[] names = new String[] {
            "Flowers", "Bulbs",
        };
        for (String name : names)
            categoryRepository.saveAndFlush(new Category(name));
    }
}
