package ua.tqs.g6.project.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ua.tqs.g6.project.entities.Consumer;
import ua.tqs.g6.project.entities.Product;
import ua.tqs.g6.project.entities.Sales;
import ua.tqs.g6.project.repositories.ConsumerRepository;
import ua.tqs.g6.project.repositories.ProductRepository;
import ua.tqs.g6.project.utils.Status;

import ua.tqs.g6.project.entities.Consumer;
import ua.tqs.g6.project.repositories.ConsumerRepository;

@Controller
@RequestMapping(path = "/consumer")
@Api(value = "Consumer", description = "Operations with entity consumer")
public class ConsumerController
{
	@Autowired
	private ConsumerRepository consumerRepo;
	
	@Autowired
	private ProductRepository productRepo;

	@ApiOperation(value = "Get all consumers")
	@CrossOrigin(origins = "*")
	@GetMapping
	public @ResponseBody Iterable<Consumer> findAll()
	{
		return consumerRepo.findAll();
	}

	@ApiOperation(value = "Get consumer by id")
	@GetMapping(path = "/{userId}")
	public ResponseEntity<Consumer> find(@PathVariable("userId") int userId)
	{	
		Optional<Consumer> consumer = consumerRepo.findById(userId);
		if (consumer.isPresent())
			return new ResponseEntity<>(consumer.get(), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);

	}

	@ApiOperation(value = "Add consumer to database")
	@PostMapping(consumes = "application/json")
	public @ResponseBody Consumer create(@RequestBody Consumer consumer)
	{
		return consumerRepo.saveAndFlush(consumer);
	}
	
	@PutMapping(path = "/{id}", consumes = "application/json")
	public ResponseEntity<Object> update(@RequestBody Map<String, Object> json, @PathVariable("id") int id)
	{
		Optional<Consumer> consumer = consumerRepo.findById(id);
		if (consumer.isPresent())
		{
			for (Map.Entry<String, Object> entry : json.entrySet())
				try
				{
					consumer.get().getClass().getMethod(String.format("set%s",   entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1)), entry.getValue().getClass()).invoke(consumer.get(), entry.getValue());
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException
						| NoSuchMethodException | SecurityException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			consumerRepo.saveAndFlush(consumer.get());	
		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	/*@ApiOperation(value = "Add product to cart")
	@PutMapping(path = "/{userId}/cart/{productId}")
	public ResponseEntity<Sales> addToCart(@PathVariable("userId") int userId, @PathVariable("productId") int productId)
	{
		Optional<Consumer> consumer = consumerRepo.findById(userId);
		Optional<Product> product = productRepo.findById(productId);
		if (consumer.isPresent() && product.isPresent())
		{

		}
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}

	@ApiOperation(value = "Get product in cart")
	@GetMapping(path = "/{userId}/cart")
	public ResponseEntity<Iterable<Sales>> getCartList(@PathVariable("userId") int userId)
	{
		Optional<Consumer> consumer = consumerRepo.findById(userId);
		if (consumer.isPresent())
			return new ResponseEntity<>(consumer.get().getSales().stream()
					.filter(s -> s.getStatus() == Status.PROCESSING).collect(Collectors.toList()), HttpStatus.OK);
		return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	}*/
}
