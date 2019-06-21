package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
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

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Before
    public void beforeEach() {
        consumerRepository.deleteAll();
        producerRepository.deleteAll();
    }

    @Test
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
    }

    @Test
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
    }

    @Test
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
}