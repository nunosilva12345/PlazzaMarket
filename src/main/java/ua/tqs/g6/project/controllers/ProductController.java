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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import ua.tqs.g6.project.entities.Product;
import ua.tqs.g6.project.repositories.ProductRepository;

@Controller
@RequestMapping(path = "/product")
@Api(value = "Product", description = "Operations with entity product")
public class ProductController
{	
	@Autowired
	private ProductRepository productRepository;

	@DeleteMapping
	@ApiOperation(value = "Delete all products")
	public Iterable<Product> deleteAll()
	{
		Iterable<Product> all = productRepository.findAll();
		productRepository.deleteAll();
		return all;
	}
	
	@DeleteMapping(path = "/{id}")
	@ApiOperation(value = "Delete product by id")
	public ResponseEntity<Product> delete(@PathVariable("id") int id)
	{
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent())
		{
			productRepository.delete(product.get());
			return new ResponseEntity<>(product.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Get all product")
	public @ResponseBody Iterable<Product> findAll()
	{
		return productRepository.findAll();
	}

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Get product by id")
	public ResponseEntity<Product> findById(@PathVariable("id") int id)
	{
		Optional<Product> product = productRepository.findById(id);
		if (product.isPresent())
			return new ResponseEntity<>(product.get(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping(path = "/{id}", consumes = "application/json")
	@ApiOperation(value = "Update selected product by id in database")
	public ResponseEntity<Product> update(@RequestBody Map<String, Object> json, @PathVariable("id") int id)
	{
		Optional<Product> optional = productRepository.findById(id);
		if (!optional.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Product product = optional.get();
		for (Map.Entry<String, Object> entry : json.entrySet())
			try
			{
				String key = entry.getKey();
				Object value = entry.getValue();
				Product.class.getMethod(String.format("set%s", key.substring(0, 1).toUpperCase() + key.substring(1)),
						value.getClass()).invoke(product, value);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		return new ResponseEntity<>(productRepository.saveAndFlush(product), HttpStatus.OK);
	}
}