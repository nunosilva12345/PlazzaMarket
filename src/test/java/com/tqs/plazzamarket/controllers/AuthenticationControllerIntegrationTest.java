package com.tqs.plazzamarket.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Admin;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.repositories.AdminRepository;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;

import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
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

@SpringBootTest (webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class AuthenticationControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Before
    public void beforeEach() {
        consumerRepository.deleteAll();
        producerRepository.deleteAll();
    }

    @Test
    @Transactional
    public void testConsumerRegister() throws Exception {
        Consumer consumer = new Consumer();
        consumer.setUsername("luiso");
        consumer.setName("Luis Oliveira");
        consumer.setEmail("luis@ua.pt");
        consumer.setPassword("12345678");
        consumer.setAddress("Aveiro");
        consumer.setZipCode("3060-500");
        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/register/consumer/").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(consumer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Assert.assertEquals(consumer, mapper.readValue(result, Consumer.class));
        Optional<Consumer> optional = consumerRepository.findById(consumer.getUsername());
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(consumer, optional.get());
    }

    @Test
    @Transactional
    public void testProducerRegister() throws Exception {
        Producer producer = new Producer();
        producer.setUsername("luiso");
        producer.setName("Luis Oliveira");
        producer.setEmail("luis@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Aveiro");
        producer.setZipCode("3060-500");
        producer.setWebsite("https://www.example.com");
        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/register/producer/").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(producer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Assert.assertEquals(producer, mapper.readValue(result, Producer.class));
        Optional<Producer> optional = producerRepository.findById(producer.getUsername());
        Assert.assertTrue(optional.isPresent());
        Assert.assertEquals(producer, optional.get());
    }

    @Test
    @Transactional
    public void testFailRegister() throws Exception {
        testConsumerRegister();
        Producer producer = new Producer();
        producer.setUsername("luiso");
        producer.setName("Luis Oliveira");
        producer.setEmail("luis@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Aveiro");
        producer.setZipCode("3060-500");
        producer.setWebsite("https://www.example.com");
        mvc.perform(MockMvcRequestBuilders.post("/api/register/producer/").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(producer))).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    public void testLoginConsumer() throws Exception {
        Consumer consumer = new Consumer();
        consumer.setUsername("luiso");
        consumer.setName("Luis Oliveira");
        consumer.setEmail("luis@ua.pt");
        consumer.setPassword("12345678");
        consumer.setAddress("Aveiro");
        consumer.setZipCode("3060-500");
        consumerRepository.saveAndFlush(consumer);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", consumer.getUsername());
        credentials.put("password", consumer.getPassword());

        mvc.perform(MockMvcRequestBuilders.post("/api/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(credentials))).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testLoginProducer() throws  Exception {
        Producer producer = new Producer();
        producer.setUsername("luiso");
        producer.setName("Luis Oliveira");
        producer.setEmail("luis@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Aveiro");
        producer.setZipCode("3060-500");
        producer.setWebsite("https://www.example.com");
        producerRepository.saveAndFlush(producer);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", producer.getUsername());
        credentials.put("password", producer.getPassword());

        mvc.perform(MockMvcRequestBuilders.post("/api/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(credentials))).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    public void testLoginAdmin() throws  Exception {
        Admin admin = new Admin();
        admin.setUsername("luiso");
        admin.setPassword("12345678");
        adminRepository.saveAndFlush(admin);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", admin.getUsername());
        credentials.put("password", admin.getPassword());

        mvc.perform(MockMvcRequestBuilders.post("/api/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(credentials))).andExpect(MockMvcResultMatchers.status().isOk());
    }
}