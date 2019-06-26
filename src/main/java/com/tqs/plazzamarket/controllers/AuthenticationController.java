package com.tqs.plazzamarket.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.utils.BaseUser;
import com.tqs.plazzamarket.utils.Cart;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api")
public class AuthenticationController {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @PostMapping(path = "/register/consumer", consumes = "application/json")
    public ResponseEntity registerConsumer(@RequestBody Map<String, Object> consumerMap) {
        @Valid
        Consumer consumer = mapper.convertValue(consumerMap, Consumer.class);
        ResponseEntity<? extends Object> re = usernameExists(consumer);
        return re != null ? re : new ResponseEntity<>(consumerRepository.saveAndFlush(consumer), HttpStatus.CREATED);
    }

    @PostMapping(path = "/register/producer")
    public ResponseEntity registerProducer(@RequestBody Map<String, Object> producerMap) {
        @Valid
        Producer producer = mapper.convertValue(producerMap, Producer.class);
        ResponseEntity<? extends Object> re = usernameExists(producer);
        return re != null ? re : new ResponseEntity<>(producerRepository.saveAndFlush(producer), HttpStatus.CREATED);
    }

    @PostMapping(path = "/login", consumes = "application/json")
    public ResponseEntity login(@RequestBody Map<String, Object> credentials, HttpSession httpSession) {
        final String uKey = "username";
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

    @PostMapping(path = "/logout")
    public ResponseEntity logout(HttpSession httpSession) {
        httpSession.invalidate();
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    private ResponseEntity<? extends Object> usernameExists(BaseUser bu) {
        if (consumerRepository.existsById(bu.getUsername()) || producerRepository.existsById(bu.getUsername())) {
            Map<String, String> error = new HashMap<>();
            error.put("username", "Username already in use!");
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}