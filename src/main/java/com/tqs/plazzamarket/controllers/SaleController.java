package com.tqs.plazzamarket.controllers;


import com.tqs.plazzamarket.entities.Sale;
import com.tqs.plazzamarket.repositories.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/sale")
public class SaleController {

    @Autowired
    private SaleRepository saleRepository;

    @GetMapping(value = "/all")
    public ResponseEntity<List<Sale>> findAll() {
        return new ResponseEntity(saleRepository.findAll(), HttpStatus.OK);
    }
}
