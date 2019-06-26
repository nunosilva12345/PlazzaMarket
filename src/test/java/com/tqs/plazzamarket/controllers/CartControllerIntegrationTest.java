package com.tqs.plazzamarket.controllers;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.utils.Cart;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ConsumerRepository consumerRepository;

    private Consumer consumer;

    private Product product;

    private Cart cart;

    private Map<String, Object> map;

    @Before
    public void beforeEach() {
        product = new Product();
        product.setId(1);
        product.setQuantity(4);
        product.setPrice(5);
        product.setDescription("test");
        product.setName("Potato");
        productRepository.saveAndFlush(product);

        consumer = new Consumer();
        consumer.setUsername("luiso");
        consumer.setName("Luis Oliveira");
        consumer.setEmail("luis@ua.pt");
        consumer.setPassword("12345678");
        consumer.setAddress("Aveiro");
        consumer.setZipCode("3060-500");
        consumerRepository.saveAndFlush(consumer);

        cart = new Cart();

        map = new HashMap<>();
        Double quantity = 4.;
        Integer productId = 1;
        map.put("quantity", quantity);
        map.put("productId", productId);
    }

    @Test
    public void testAddProductToCard() throws UnsupportedEncodingException, JsonProcessingException, Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/cart/add").contentType(MediaType.APPLICATION_JSON).accept("application/json;charset=UTF-8")
                        .content(mapper.writeValueAsString(map)).sessionAttr("cart", cart).sessionAttr("user", consumer))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
                .getResponse().getContentAsString();
    }

    

}