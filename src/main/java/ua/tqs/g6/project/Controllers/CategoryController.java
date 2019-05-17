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
import ua.tqs.g6.project.repositories.CategoryRepository;

@Controller
@RequestMapping(path = "/category")
@Api(value="Category", description="Operations with entity category")
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepo;
    
    @ApiOperation(value = "Get all categories")
    @CrossOrigin(origins = "*")
    @GetMapping
    public @ResponseBody Iterable<Category> findAll() {
        return categoryRepo.findAll();
    }

    @ApiOperation(value = "Get category by id")
    @GetMapping(path = "/{id}")
    public Category find(@PathVariable("id") int id) {
        Category category = null;
        category = categoryRepo.getOne(id);
        return category;
       
    }
    
    @ApiOperation(value = "Add category to database")
    @PostMapping(consumes = "application/json")
    public Category create(@RequestBody Category category) {
        return categoryRepo.save(category);
    }
    
}
