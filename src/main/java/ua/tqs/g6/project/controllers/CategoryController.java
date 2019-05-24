/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.tqs.g6.project.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import ua.tqs.g6.project.entities.Product;
import ua.tqs.g6.project.entities.Category;
import ua.tqs.g6.project.entities.ProductName;

import ua.tqs.g6.project.repositories.ProductRepository;
import ua.tqs.g6.project.repositories.CategoryRepository;
import ua.tqs.g6.project.repositories.ProductNameRepository;

@Controller
@RequestMapping(path = "/category")
@Api(value = "Category", description = "Operations with entity category")
public class CategoryController
{
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;
	
	@Autowired
	private ProductNameRepository productNameRepository;

	@ApiOperation(value = "Add category to database")
	@PostMapping(consumes = "application/json")
	public @ResponseBody Category create(@RequestBody Category category)
	{
		return categoryRepository.saveAndFlush(category);
	}
	
	@DeleteMapping
	@ApiOperation(value = "Delete all categories")
	public Iterable<Category> deleteAll()
	{
		Iterable<Category> all = categoryRepository.findAll();
		categoryRepository.deleteAll();
		return all;
	}

	@DeleteMapping(path = "/{id}")
	@ApiOperation(value = "Delete category by id")
	public ResponseEntity<Category> delete(@PathVariable("id") int id)
	{
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isPresent())
		{
			categoryRepository.delete(category.get());
			return new ResponseEntity<>(category.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Get all categories")
	public @ResponseBody Iterable<Category> findAll()
	{
		return categoryRepository.findAll();
	}

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Get category by id")
	public ResponseEntity<Category> findById(@PathVariable("id") int id)
	{
		Optional<Category> category = categoryRepository.findById(id);
		if (category.isPresent())
			return new ResponseEntity<>(category.get(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping(path = "/{id}", consumes = "application/json")
	@ApiOperation(value = "Update selected category by id in database")
	public ResponseEntity<Category> update(@RequestBody Map<String, Object> json, @PathVariable("id") int id)
	{
		Optional<Category> optional = categoryRepository.findById(id);
		if (!optional.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Category category = optional.get();
		for (Map.Entry<String, Object> entry : json.entrySet())
			try
			{
				String key = entry.getKey();
				Object value = entry.getValue();
				Category.class.getMethod(String.format("set%s", key.substring(0, 1).toUpperCase() + key.substring(1)),
						value.getClass()).invoke(category, value);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		return new ResponseEntity<>(categoryRepository.saveAndFlush(category), HttpStatus.OK);
	}
	
	@PostMapping(path = "/{id}/productName", consumes = "application/json")
	@ApiOperation(value = "Create productName for given category")
	public ResponseEntity<ProductName> createProductName(@RequestBody ProductName productName, @PathVariable("id") int id)
	{
		Optional<Category> category = categoryRepository.findById(id);
		if (!category.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		productName.setCategory(category.get());
		return new ResponseEntity<>(productNameRepository.saveAndFlush(productName), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}/productName")
	@ApiOperation(value = "Delete all productName for give category")
	public ResponseEntity<Iterable<ProductName>> deleteAllProductName(@PathVariable("id") int id)
	{
		Optional<Category> category = categoryRepository.findById(id);
		if (!category.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Iterable<ProductName> all = category.get().getProductNames();
		productNameRepository.deleteAll(all);
		return new ResponseEntity<>(all, HttpStatus.OK);
	}

	@GetMapping(path = "{id}/productName")
	@ApiOperation(value = "Get all productNames for given category")
	public ResponseEntity<Iterable<ProductName>> findAllProductName(@PathVariable("id") int id)
	{
		Optional<Category> category = categoryRepository.findById(id);
		if (!category.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(category.get().getProductNames(), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}/product")
	@ApiOperation(value = "Delete all product for given category")
	public ResponseEntity<Iterable<Product>> deleteAllProduct(@PathVariable("id") int id)
	{
		Optional<Category> category = categoryRepository.findById(id);
		if (!category.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Iterable<ProductName> productNames = category.get().getProductNames();
		Iterable<Product> all = productRepository.findAll(productNames);
		productRepository.deleteAll(all);
		return new ResponseEntity<>(all, HttpStatus.OK);
	}
	
	@GetMapping(path = "{id}/product")
	@ApiOperation(value = "Get all products for given category")
	public ResponseEntity<Iterable<Product>> findAllProduct(@PathVariable("id") int id)
	{
		Optional<Category> category = categoryRepository.findById(id);
		if (!category.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productRepository.findAll(category.get().getProductNames()), HttpStatus.OK);
	}
}