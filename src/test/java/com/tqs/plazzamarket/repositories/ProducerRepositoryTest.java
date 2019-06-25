package com.tqs.plazzamarket.repositories;

import org.junit.Test;

import java.util.List;
import java.util.Optional;

import com.tqs.plazzamarket.entities.Producer;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;


@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:test.properties")
public class ProducerRepositoryTest {
    
    private Producer producer;
    
    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void beforeEach() {
        producer = new Producer();
        producer.setUsername("luiso");
        producer.setName("Luis Oliveira");
        producer.setEmail("luis@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Aveiro");
        producer.setZipCode("3060-500");
        producer.setWebsite("https://www.example.com");
        entityManager.persistAndFlush(producer);
    }

    @Test
    public void whenGetById_returnConsumer() {
        Optional<Producer> found = producerRepository.findById(producer.getUsername());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(producer, found.get());
    }

    @Test
    public void whenFindAll_containsConsumer() {
        List<Producer> found = producerRepository.findAll();
        Assert.assertTrue(found.contains(producer));
    }

    @Test
    public void failWhenGetById() {
        Optional<Producer> found = producerRepository.findById("test");
        Assert.assertFalse(found.isPresent());
    }

    @After
    public void afterEach() {
        entityManager.clear();
    }
}
