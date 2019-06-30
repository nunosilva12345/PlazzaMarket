package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Receipt;
import com.tqs.plazzamarket.repositories.ReceiptRepository;
import com.tqs.plazzamarket.repositories.SaleRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@WebMvcTest(ReceiptsController.class)
public class ReceiptControllerUnitTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ReceiptRepository receiptRepository;


    @Test
    public void findAllTest() throws Exception{
        Receipt receipt = new Receipt();
        receipt.setProductName("Potato");
        receipt.setQuantity(20.);
        receipt.setPrice(3);
        List<Receipt> receipts= new ArrayList<>();
        receipts.add(receipt);
        BDDMockito.given(receiptRepository.findAll()).willReturn(receipts);
        String response = mvc
                .perform(MockMvcRequestBuilders.get("/api/receipts"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
    }
}
