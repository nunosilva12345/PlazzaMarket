package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Receipt;
import com.tqs.plazzamarket.repositories.ReceiptRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@Api(tags = { "Receipts" })
@SwaggerDefinition(tags = {
    @Tag(name = "Receipts", description = "Receipt Operations")
})
public class ReceiptsController {
    @Autowired
    private ReceiptRepository receiptRepository;

    @ApiOperation(value = "List all consumers")
    @GetMapping("/receipts")
    public ResponseEntity<List<Receipt>> listReceipts() {
        return new ResponseEntity<>(receiptRepository.findAll(), HttpStatus.OK);
    }
}
