package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;

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
import org.junit.Assert;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
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

        System.out.println(mapper.writeValueAsString(productJSON));

        mvc
            .perform(MockMvcRequestBuilders.post("/api/products/add").contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(productJSON)))
            .andExpect(MockMvcResultMatchers.status().isCreated());

        //Optional<Product> optional = productRepository.findById(0);
        //Assert.assertTrue(optional.isPresent());
    }
    
    
    
    @Test
    @Transactional
    public void testRemoveProduct() throws Exception {
        Product product = new Product();
        product.setQuantity(4);
        product.setPrice(5);
        product.setDescription("test");
        product.setName("Potato");
        
        System.out.println("ID: " + product.getId());
        
        
        mvc
                .perform(MockMvcRequestBuilders.get("/api/products/remove/1")).andExpect(MockMvcResultMatchers.status().isOk());

        Optional<Product> optional = productRepository.findById(product.getId());
        int size_beforeDelete = (int) productRepository.count();

        System.out.println("PRODUCT: " + product);
        System.out.println("size: " + size_beforeDelete);
        
        
        Assert.assertTrue(optional.isPresent());

        if (optional.isPresent()) {
            System.out.println("entrou no if");
            productRepository.deleteById(product.getId());
        }
        
        int size_atferDelete = (int) productRepository.count();
        
        System.out.println("size: " + size_atferDelete);

        //nao existe depois do remove, logo reusltou com sucesso
        Assert.assertEquals(size_beforeDelete, size_atferDelete + 1);

    }



}
