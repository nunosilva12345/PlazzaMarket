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
import ua.tqs.g6.project.entities.ProductName;
import ua.tqs.g6.project.repositories.ProductNameRepository;

@Controller
@RequestMapping(path = "/productName")
@Api(value = "ProductName", description = "Operations with entity productName")
public class ProductNameController
{
	@Autowired
	private ProductNameRepository productNameRepository;

	@DeleteMapping
	@ApiOperation(value = "Delete all productNames")
	public Iterable<ProductName> deleteAll()
	{
		Iterable<ProductName> all = productNameRepository.findAll();
		productNameRepository.deleteAll();
		return all;
	}
	
	@DeleteMapping(path = "/{id}")
	@ApiOperation(value = "Delete productName by id")
	public ResponseEntity<ProductName> delete(@PathVariable("id") int id)
	{
		Optional<ProductName> productName = productNameRepository.findById(id);
		if (productName.isPresent())
		{
			productNameRepository.delete(productName.get());
			return new ResponseEntity<>(productName.get(), HttpStatus.OK);
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@GetMapping
	@CrossOrigin(origins = "*")
	@ApiOperation(value = "Get all productNames")
	public @ResponseBody Iterable<ProductName> findAll()
	{
		return productNameRepository.findAll();
	}

	@GetMapping(path = "/{id}")
	@ApiOperation(value = "Get productName by id")
	public ResponseEntity<ProductName> findById(@PathVariable("id") int id)
	{
		Optional<ProductName> productName = productNameRepository.findById(id);
		if (productName.isPresent())
			return new ResponseEntity<>(productName.get(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@PutMapping(path = "/{id}", consumes = "application/json")
	@ApiOperation(value = "Update selected productName by id in database")
	public ResponseEntity<ProductName> update(@RequestBody Map<String, Object> json, @PathVariable("id") int id)
	{
		Optional<ProductName> optional = productNameRepository.findById(id);
		if (!optional.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		ProductName productName = optional.get();
		for (Map.Entry<String, Object> entry : json.entrySet())
			try
			{
				String key = entry.getKey();
				Object value = entry.getValue();
				ProductName.class
						.getMethod(String.format("set%s", key.substring(0, 1).toUpperCase() + key.substring(1)),
								value.getClass())
						.invoke(productName, value);
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
				e.printStackTrace();
			}
		return new ResponseEntity<>(productNameRepository.saveAndFlush(productName), HttpStatus.OK);
	}
	
	@GetMapping(path = "{id}/product")
	@ApiOperation(value = "Get all products for given productName")
	public ResponseEntity<Iterable<Product>> findAllProduct(@PathVariable("id") int id)
	{
		Optional<ProductName> productName = productNameRepository.findById(id);
		if (!productName.isPresent())
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		return new ResponseEntity<>(productName.get().getProducts(), HttpStatus.OK);
	}
}