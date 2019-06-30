package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Admin;
import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/api/category")
@Api(tags = { "Category" })
@SwaggerDefinition(tags = {
    @Tag(name = "Category", description = "Operations with categories")
})
public class CategoryController {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @ApiOperation(value = "List all categories")
    @CrossOrigin("/*")
    @GetMapping()
    public ResponseEntity<List<Category>> findAll() {
        return new ResponseEntity<>(categoryRepository.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Create new category")
    @PostMapping(path = "/addcategory", consumes = "application/json")
    public ResponseEntity<Category> createCategory(@RequestBody Map<String, Object> categoryJSON, HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("user").getClass() != Admin.class)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Category category = mapper.convertValue(categoryJSON, Category.class);
        category = categoryRepository.saveAndFlush(category);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }

    @ApiOperation(value = "Create new category")
    @DeleteMapping(path = "/delete/{category}")
    public ResponseEntity<Boolean> deleteCategory(@PathVariable("category") String category, HttpSession session) {
        if (session.getAttribute("user") == null || session.getAttribute("user").getClass() != Admin.class)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        if (!categoryRepository.findById(category).isPresent())
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        categoryRepository.deleteById(category);
        return new ResponseEntity<>(true, HttpStatus.OK);
    }
}
