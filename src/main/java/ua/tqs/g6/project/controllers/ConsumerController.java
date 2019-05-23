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

import ua.tqs.g6.project.entities.Consumer;
import ua.tqs.g6.project.repositories.ConsumerRepository;

@Controller
@RequestMapping(path = "/consumer")
@Api(value = "Consumer", description="Operations with entity consumer")
public class ConsumerController
{
    @Autowired
    private ConsumerRepository consumerRepository;

    @ApiOperation(value = "Add consumer to database")
	@PostMapping(consumes = "application/json")
	public @ResponseBody Consumer create(@RequestBody Consumer consumer)
	{
		return consumerRepository.saveAndFlush(consumer);
    }
    
    @DeleteMapping(path = "/{id}")
    @ApiOperation(value = "Delete consumer by id")
    public ResponseEntity<Consumer> delete(@PathVariable("id") int id)
    {
        Optional<Consumer> consumer = consumerRepository.findById(id);
        if (consumer.isPresent())
        {
            consumerRepository.delete(consumer.get());
		    return new ResponseEntity<>(consumer.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping
    @CrossOrigin(origins = "*")
    @ApiOperation(value = "Get all consumers")
    public @ResponseBody Iterable<Consumer> findAll()
    {
        return consumerRepository.findAll();
    }

	@GetMapping(path = "/{id}")
    @ApiOperation(value = "Get consumer by id")
	public ResponseEntity<Consumer> findById(@PathVariable("id") int id)
	{	
		Optional<Consumer> consumer = consumerRepository.findById(id);
		if (consumer.isPresent())
			return new ResponseEntity<>(consumer.get(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    @ApiOperation(value = "Update selected consumer by id in database")
	public ResponseEntity<Consumer> update(@RequestBody Map<String, Object> json, @PathVariable("id") int id)
	{
		Optional<Consumer> optional = consumerRepository.findById(id);
        if (!optional.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        Consumer consumer = optional.get();
		for (Map.Entry<String, Object> entry : json.entrySet())
			try
			{
                String key = entry.getKey();
                Object value = entry.getValue();
				Consumer.class.getMethod(String.format("set%s", key.substring(0, 1).toUpperCase() + key.substring(1)), value.getClass()).invoke(consumer, value);
            } 
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
            	e.printStackTrace();
			}
		return new ResponseEntity<>(consumerRepository.saveAndFlush(consumer), HttpStatus.OK);
	}
}