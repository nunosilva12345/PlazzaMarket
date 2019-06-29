package com.tqs.plazzamarket.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Validator {
    @Autowired
    private javax.validation.Validator validator;

    public <T> Map<String, String> validate(T object) {
        Set<ConstraintViolation<T>> violations = validator.validate(object);
        if (violations.size() != 0) {
            Map<String, String> result = new HashMap<>();
            for (ConstraintViolation<T> violation : violations) {
                List<String> nodes = new ArrayList<>();
                violation.getPropertyPath().forEach(entry -> nodes.add(entry.getName()));
                result.put(String.join(".", nodes.toArray(new String[nodes.size()])), violation.getMessage());
            }
            return result;
        }
        return null;
    }
}