package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;


    @Before
    public void beforeEach() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateProduct() throws Exception{
        Map<String, Object> productJSON = new HashMap<>();
        productJSON.put("name", "Potato");
        productJSON.put("quantity", "4");
        productJSON.put("price", "5");
        productJSON.put("description", "test");

        Product p = new Product();
        p.setName("Potato");
        p.setQuantity(4);
        p.setPrice(5);
        p.setDescription("test");
        p.setId(1);

        String result = mvc
            .perform(MockMvcRequestBuilders.post("/api/products/add").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(productJSON)))
            .andExpect(MockMvcResultMatchers.status().isCreated())
            .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andReturn().getResponse().getContentAsString();
        Assert.assertEquals(p, mapper.readValue(result, Product.class));

        Optional<Product> optional = productRepository.findById(1);
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(p, optional.get());
    }

}
