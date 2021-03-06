package com.tqs.plazzamarket.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Admin;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.repositories.AdminRepository;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.utils.BaseUser;
import com.tqs.plazzamarket.utils.Cart;

import com.tqs.plazzamarket.services.Validator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api")
@Api(tags = { "Authentication" })
@SwaggerDefinition(tags = {
    @Tag(name = "Authentication", description = "Authentication Operations")
})
public class AuthenticationController {

    private static final String USERNAME = "username";

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private Validator validator;

    @ApiOperation(value = "Register consumer", response = Consumer.class)
    @PostMapping(path = "/register/consumer", consumes = "application/json")
    public ResponseEntity registerConsumer(@RequestBody Map<String, Object> consumerMap) {
        Consumer consumer = mapper.convertValue(consumerMap, Consumer.class);
        Map<String, String> errors = validator.validate(consumer);
        if (errors != null)
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        ResponseEntity<? extends Object> re = usernameExists(consumer);
        return re != null ? re : new ResponseEntity<>(consumerRepository.saveAndFlush(consumer), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Register producer", response = Producer.class)
    @PostMapping(path = "/register/producer")
    public ResponseEntity registerProducer(@RequestBody Map<String, Object> producerMap) {
        Producer producer = mapper.convertValue(producerMap, Producer.class);
        Map<String, String> errors = validator.validate(producer);
        if (errors != null)
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        ResponseEntity<? extends Object> re = usernameExists(producer);
        return re != null ? re : new ResponseEntity<>(producerRepository.saveAndFlush(producer), HttpStatus.CREATED);
    }

    @ApiOperation(value = "Login")
    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity login(@RequestBody Map<String, Object> credentials, HttpSession httpSession) {
        final String uKey = USERNAME;
        final String pKey = "password";
        final String userAttr = "user";

        if (!credentials.containsKey(uKey) || !credentials.containsKey(pKey))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String username = credentials.get(uKey).toString();
        String password = credentials.get(pKey).toString();

        Optional<Consumer> consumerOpt = consumerRepository.findById(username);
        if (consumerOpt.isPresent()) {
            Consumer consumer = consumerOpt.get();
            if (!consumer.getPassword().equals(password))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            httpSession.setAttribute(userAttr, consumer);
            httpSession.setAttribute("cart", new Cart());
            return new ResponseEntity<>(HttpStatus.OK);
        }

        Optional<Producer> producerOpt = producerRepository.findById(username);
        if (producerOpt.isPresent()) {
            Producer producer = producerOpt.get();
            if (!producer.getPassword().equals(password))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            httpSession.setAttribute(userAttr, producer);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ApiOperation(value = "Login Admin")
    @PostMapping(path = "/admin/login", consumes = "application/json")
    public ResponseEntity adminLogin(@RequestBody Map<String, Object> credentials, HttpSession httpSession) {
        final String uKey = USERNAME;
        final String pKey = "password";
        final String userAttr = "user";

        if (!credentials.containsKey(uKey) || !credentials.containsKey(pKey))
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        String username = credentials.get(uKey).toString();
        String password = credentials.get(pKey).toString();

        Optional<Admin> adminOpt = adminRepository.findById(username);
        if (adminOpt.isPresent()) {
            Admin admin = adminOpt.get();
            if (!admin.getPassword().equals(password))
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            httpSession.setAttribute(userAttr, admin);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


    @ApiOperation(value = "Logout")
    @PostMapping(path = "/logout")
    public ResponseEntity logout(HttpSession httpSession) {
        if (httpSession.getAttribute("user") == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        httpSession.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ResponseEntity<? extends Object> usernameExists(BaseUser bu) {
        if (consumerRepository.existsById(bu.getUsername()) || producerRepository.existsById(bu.getUsername())) {
            Map<String, String> error = new HashMap<>();
            error.put(USERNAME, "Username already in use!");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return null;
    }

    @ApiOperation(value = "List all producers")
    @GetMapping(path = "/producers")
    public ResponseEntity<List<Producer>> listProducers() {
        return new ResponseEntity<>(producerRepository.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "List all consumers")
    @GetMapping(path = "/consumers")
    public ResponseEntity<List<Consumer>> listConsumers() {
        return new ResponseEntity<>(consumerRepository.findAll(), HttpStatus.OK);
    }


}