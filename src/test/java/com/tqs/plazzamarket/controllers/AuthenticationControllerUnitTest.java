package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Admin;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.repositories.AdminRepository;
import com.tqs.plazzamarket.repositories.ConsumerRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.services.Validator;

import org.junit.Test;
import org.junit.Assert;
import org.mockito.BDDMockito;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(AuthenticationController.class)
public class AuthenticationControllerUnitTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ConsumerRepository consumerRepository;

    @MockBean
    private ProducerRepository producerRepository;

    @MockBean
    private AdminRepository adminRepository;

    @MockBean
    private Validator validator;

    @Test
    public void testConsumerRegister() throws Exception {
        Consumer consumer = new Consumer();
        consumer.setUsername("luiso");
        consumer.setName("Luis Oliveira");
        consumer.setEmail("luis@ua.pt");
        consumer.setPassword("12345678");
        consumer.setAddress("Aveiro");
        consumer.setZipCode("3060-500");
        BDDMockito.given(validator.validate(consumer)).willReturn(null);
        BDDMockito.given(consumerRepository.saveAndFlush(consumer)).willReturn(consumer);
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
        BDDMockito.given(validator.validate(producer)).willReturn(null);
        BDDMockito.given(producerRepository.saveAndFlush(producer)).willReturn(producer);
        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/register/producer/").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(producer)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Assert.assertEquals(producer, mapper.readValue(result, Producer.class));
    }

    @Test
    public void testLoginProducer() throws Exception {
        Producer producer = new Producer();
        producer.setUsername("luiso");
        producer.setName("Luis Oliveira");
        producer.setEmail("luis@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Aveiro");
        producer.setZipCode("3060-500");
        producer.setWebsite("https://www.example.com");
        BDDMockito.given(producerRepository.findById(producer.getUsername())).willReturn(Optional.of(producer));

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", producer.getUsername());
        credentials.put("password", producer.getPassword());

        mvc.perform(MockMvcRequestBuilders.post("/api/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(credentials))).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLoginConsumer() throws Exception {
        Consumer consumer = new Consumer();
        consumer.setUsername("luiso");
        consumer.setName("Luis Oliveira");
        consumer.setEmail("luis@ua.pt");
        consumer.setPassword("12345678");
        consumer.setAddress("Aveiro");
        consumer.setZipCode("3060-500");
        BDDMockito.given(consumerRepository.findById(consumer.getUsername())).willReturn(Optional.of(consumer));

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", consumer.getUsername());
        credentials.put("password", consumer.getPassword());

        mvc.perform(MockMvcRequestBuilders.post("/api/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(credentials))).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testLoginAdmin() throws Exception {
        Admin admin= new Admin();
        admin.setUsername("luiso");
        admin.setPassword("12345678");
        BDDMockito.given(adminRepository.findById(admin.getUsername())).willReturn(Optional.of(admin));

        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", admin.getUsername());
        credentials.put("password", admin.getPassword());

        mvc.perform(MockMvcRequestBuilders.post("/api/login").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(credentials))).andExpect(MockMvcResultMatchers.status().isOk());
    }
}