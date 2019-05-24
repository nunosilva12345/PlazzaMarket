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
import ua.tqs.g6.project.entities.Producer;
import ua.tqs.g6.project.entities.ProductName;

import ua.tqs.g6.project.repositories.ProductRepository;
import ua.tqs.g6.project.repositories.CategoryRepository;
import ua.tqs.g6.project.repositories.ProducerRepository;
import ua.tqs.g6.project.repositories.ProductNameRepository;

@Controller
@RequestMapping(path = "/producer")
@Api(value = "Producer", description = "Operations with entity producer")
public class ProducerController
{
	@Autowired
	private ProductNameRepository productNameRepository;
	
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProducerRepository producerRepository;

	@Autowired
	private ProductRepository productRepository;

	@ApiOperation(value = "Add producer to database")
	@PostMapping(consumes = "application/json")
	public @ResponseBody Producer create(@RequestBody Producer producer)
	{
		return producerRepository.saveAndFlush(producer);
	}
	
	@DeleteMapping
	@ApiOperation(value = "Delete all producers")
	public Iterable<Producer> deleteAll()
	{
		Iterable<Producer> all = producerRepository.findAll();
		producerRepository.deleteAll();
		return all;
	}

	@DeleteMapping(path = "/{id}")
	@ApiOperation(value = "Delete producer by id")
	public ResponseEntity<Producer> delete(@PathVariable("id") int id)
	{
		Optional<Producer> producer = producerRepository.findById(id);
		if (producer.isPresent())
		{
			producerRepository.delete(producer.get());
			return new ResponseEntity<>(producer.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Get all producers")
	public @ResponseBody Iterable<Producer> findAll()
	{
		return producerRepository.findAll();
	}

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Get producer by id")
	public ResponseEntity<Producer> findById(@PathVariable("id") int id)
	{
		Optional<Producer> producer = producerRepository.findById(id);
		if (producer.isPresent())
			return new ResponseEntity<>(producer.get(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping(path = "/{id}", consumes = "application/json")
	@ApiOperation(value = "Update selected producer by id in database")
	public ResponseEntity<Producer> update(@RequestBody Map<String, Object> json, @PathVariable("id") int id)
	{
		Optional<Producer> optional = producerRepository.findById(id);
		if (!optional.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Producer producer = optional.get();
		for (Map.Entry<String, Object> entry : json.entrySet())
			try
			{
				String key = entry.getKey();
				Object value = entry.getValue();
				Producer.class.getMethod(String.format("set%s", key.substring(0, 1).toUpperCase() + key.substring(1)),
						value.getClass()).invoke(producer, value);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		return new ResponseEntity<>(producerRepository.saveAndFlush(producer), HttpStatus.OK);
	}

	// Product related endpoints

	@PostMapping(path = "/{id}/product")
	@ApiOperation(value = "Create product for given producer")
	public ResponseEntity<Product> createProduct(@RequestBody Product product, @PathVariable("id") int id)
	{
		if (product.getProductName() == null || product.getCategory() == null)
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Optional<Producer> producer = producerRepository.findById(id);
		if (!producer.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Optional<ProductName> productName = productNameRepository.findById(product.getProductName().getId());
		if (!productName.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Optional<Category> category = categoryRepository.findById(product.getCategory().getId());
		if (!category.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		product.setProducer(producer.get());
		product.setCategory(category.get());
		product.setProductName(productName.get());
		return new ResponseEntity<>(productRepository.saveAndFlush(product), HttpStatus.OK);
	}
	
	@DeleteMapping(path = "/{id}/product")
	@ApiOperation(value = "Delete all product for give producer")
	public ResponseEntity<Iterable<Product>> deleteAllProduct(@PathVariable("id") int id)
	{
		Optional<Producer> producer = producerRepository.findById(id);
		if (!producer.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		Iterable<Product> all = producer.get().getProducts();
		productRepository.deleteAll(all);
		return new ResponseEntity<>(all, HttpStatus.OK);
	}

	@GetMapping(path = "{id}/product")
	@ApiOperation(value = "Get all products for given producer")
	public ResponseEntity<Iterable<Product>> findAllProduct(@PathVariable("id") int id)
	{
		Optional<Producer> producer = producerRepository.findById(id);
		if (!producer.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(producer.get().getProducts(), HttpStatus.OK);
	}
}