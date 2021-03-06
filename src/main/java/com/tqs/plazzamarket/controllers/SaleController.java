package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Receipt;
import com.tqs.plazzamarket.entities.Sale;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ReceiptRepository;
import com.tqs.plazzamarket.repositories.SaleRepository;
import com.tqs.plazzamarket.utils.Status;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/api/sale")
@Api(tags = { "Sale" })
@SwaggerDefinition(tags = {
    @Tag(name = "Sale", description = "Operations with sales")
})
public class SaleController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @GetMapping(value = "/all")
    public ResponseEntity<List<Sale>> findAll() {
        return new ResponseEntity(saleRepository.findAll(), HttpStatus.OK);
    }

    @ApiOperation(value = "Accept sale by id")
    @GetMapping(value = "/accept/{id}")
    public ResponseEntity<Integer> acceptSale(@ApiParam("sale id") @PathVariable int id, HttpSession session) {
        if (session.getAttribute("user") == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Optional<Sale> sale = saleRepository.findById(id);
        if (!sale.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        sale.get().setStatus(Status.CONFIRMED);
        saleRepository.saveAndFlush(sale.get());
        double price = sale.get().getQuantity()*sale.get().getProduct().getPrice();
        Receipt receipt = new Receipt();
        receipt.setPrice(price);
        receipt.setConsumer(sale.get().getConsumer());
        receipt.setProducer(producerRepository.getOne(((Producer)session.getAttribute("user")).getUsername()));
        receipt.setQuantity(sale.get().getQuantity());
        receipt.setProductName(sale.get().getProduct().getName());
        receiptRepository.saveAndFlush(receipt);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ApiOperation(value = "Reject sale by id")
    @GetMapping(value = "/reject/{id}")
    public ResponseEntity<Integer> rejectSale(@ApiParam("sale id") @PathVariable int id,  HttpSession session) {
        if (session.getAttribute("user") == null)
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        Optional<Sale> sale = saleRepository.findById(id);
        if (!sale.isPresent())
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (!sale.get().getProduct().getProducer().getUsername().equals(((Producer) session.getAttribute("user")).getUsername()))
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        sale.get().getProduct().addQuantity(sale.get().getQuantity());
        saleRepository.delete(sale.get());
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
