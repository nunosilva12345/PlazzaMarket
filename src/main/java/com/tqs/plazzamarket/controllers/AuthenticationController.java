package com.tqs.plazzamarket.controllers;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.utils.BaseUser;

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
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @PostMapping(path = "/register/consumer", consumes = "application/json")
    public ResponseEntity<? extends Object> registerConsumer(@Valid @RequestBody Consumer consumer) {
        ResponseEntity<? extends Object> re = usernameExists(consumer);
        return re != null ? re : new ResponseEntity<>(consumerRepository.saveAndFlush(consumer), HttpStatus.CREATED);
    }

    @PostMapping(path = "/register/producer")
    public ResponseEntity<? extends Object> registerProducer(@Valid @RequestBody Producer producer) {
        ResponseEntity<? extends Object> re = usernameExists(producer);
        return re != null ? re : new ResponseEntity<>(producerRepository.saveAndFlush(producer), HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
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