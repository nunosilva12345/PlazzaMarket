package com.tqs.plazzamarket.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.entities.Receipt;
import com.tqs.plazzamarket.entities.Sale;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ReceiptRepository;
import com.tqs.plazzamarket.repositories.SaleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(SaleController.class)
public class SaleControllerUnitTest {

    @Autowired
    private MockMvc mvc;


    @Autowired
    private ObjectMapper mapper;


    @MockBean
    private SaleRepository saleRepository;

    @MockBean
    private ReceiptRepository receiptRepository;

    @MockBean
    private ProductController productController;

    @MockBean
    private ProducerRepository producerController;

    @Mock
    private Sale sale;

    @Mock
    private Receipt receipt;

    @Mock
    private Producer producer;

    @Mock
    private Product product;


    @Before
    public void beforeEach() {
        int productId = 1;
        int saleId = 1;
        Mockito.when(sale.getId()).thenReturn(saleId);
        Mockito.when(sale.getProduct()).thenReturn(product);
        Mockito.when(product.getProducer()).thenReturn(producer);
        Mockito.when(sale.getQuantity()).thenReturn(4.);
        Mockito.when(product.getPrice()).thenReturn(4.);
        Mockito.when(producer.getUsername()).thenReturn("luiso");
        BDDMockito.given(saleRepository.findById(saleId)).willReturn(Optional.of(sale));
        BDDMockito.given(saleRepository.saveAndFlush(sale)).willReturn(sale);
        BDDMockito.given(receiptRepository.saveAndFlush(receipt)).willReturn(receipt);
    }


    @Test
    public void testAcept() throws Exception{
        mvc
            .perform(MockMvcRequestBuilders.get("/api/sale/accept/1")
                    .sessionAttr("user", producer))
            .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
            .getResponse().getContentAsString();
    }

    @Test
    public void testReject() throws Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.get("/api/sale/reject/1")
                        .sessionAttr("user", producer))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    }
}
