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

import ua.tqs.g6.project.entities.Producer;
import ua.tqs.g6.project.repositories.ProducerRepository;

@Controller
@RequestMapping(path = "/producer")
@Api(value = "Producer", description="Operations with entity producer")
public class ProducerController
{
    @Autowired
    private ProducerRepository producerRepository;

    @ApiOperation(value = "Add producer to database")
	@PostMapping(consumes = "application/json")
	public @ResponseBody Producer create(@RequestBody Producer producer)
	{
		return producerRepository.saveAndFlush(producer);
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
				Producer.class.getMethod(String.format("set%s", key.substring(0, 1).toUpperCase() + key.substring(1)), value.getClass()).invoke(producer, value);
            } 
            catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e)
			{
            	e.printStackTrace();
			}
		return new ResponseEntity<>(producerRepository.saveAndFlush(producer), HttpStatus.OK);
	}
}