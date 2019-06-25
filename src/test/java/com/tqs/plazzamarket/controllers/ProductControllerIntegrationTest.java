package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Producer;

import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.web.servlet.ResultActions;


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
    
    //new
    @Autowired
    private ProducerRepository producerRepository;
    
    @Before
    public void beforeEach() {
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        producerRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testCreateProduct() throws Exception{
        Map<String, Object> productJSON = new HashMap<>();
        productJSON.put("name", "Potato");
        productJSON.put("quantity", "4");
        productJSON.put("price", "5");
        productJSON.put("description", "test");


        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/products/add").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(productJSON)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Product p = new Product();
        p.setName("Potato");
        p.setQuantity(4);
        p.setPrice(5);
        p.setDescription("test");
        p.setId(mapper.readValue(result, Product.class).getId());

        Assert.assertEquals(p, mapper.readValue(result, Product.class));

        Optional<Product> optional = productRepository.findById(mapper.readValue(result, Product.class).getId());
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(p, optional.get());
    }

    
    
    
    @Test
    @Transactional
    public void testRemoveProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setQuantity(4);
        product.setPrice(5);
        product.setDescription("test");
        product.setName("Potato");
        
        productRepository.saveAndFlush(product);


        int size_beforeDelete = (int) productRepository.count();
        mvc.perform(MockMvcRequestBuilders.get("/api/products/remove/1")).andExpect(MockMvcResultMatchers.status().isOk());
        int size_atferDelete = (int) productRepository.count();

        Assert.assertEquals(size_beforeDelete - 1, size_atferDelete);

        Optional<Product> optional = productRepository.findById(product.getId());
        Assert.assertFalse(optional.isPresent());

    }
    
    @Test
    @Transactional
    public void testListProducerProducts() throws Exception {
        Producer producer = new Producer();
        producer.setUsername("nunosilva");
        producer.setName("Nuno Silva");
        producer.setEmail("nuno1@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Porto");
        producer.setZipCode("4470-587");
        producer.setWebsite("https://www.olaadeus.pt");
        producerRepository.saveAndFlush(producer);
        
        
        Product product = new Product();
        product.setQuantity(4);
        product.setPrice(5);
        product.setDescription("test");
        product.setName("Red Potato");
        product.setProducer(producerRepository.getOne(producer.getUsername()));
        productRepository.saveAndFlush(product);


        Product product1 = new Product();
        product1.setQuantity(10);
        product1.setPrice(3);
        product1.setDescription("test too");
        product1.setName("Sweet Potato");
        product1.setProducer(producerRepository.getOne(producer.getUsername()));
        productRepository.saveAndFlush(product1);
        
        System.out.println(producer);
        System.out.println("start: " + product);
        System.out.println("end: " + product1);
        System.out.println("begin");

        String responseList = mvc.perform(MockMvcRequestBuilders.get("/api/products/" + producer.getUsername())).andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        System.out.println("XAU");
        List<Object> list = mapper.readValue(responseList, List.class);
        
        System.out.println("OLAAA");
        System.out.println(producer.getUsername());
        System.out.println(product.getId());
        System.out.println(product1.getId());
        System.out.println(list);
        System.out.println(list.size());
        //testar, ver se o resultado(lista é igual a 2, numero de products do "luiso")
        Assert.assertEquals(list.size(),2);


    }
    
    
    
    
    
}
