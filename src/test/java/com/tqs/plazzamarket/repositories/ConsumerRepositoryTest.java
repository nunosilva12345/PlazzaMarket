package com.tqs.plazzamarket.repositories;

import java.util.List;
import java.util.Optional;

import com.tqs.plazzamarket.entities.Consumer;

import org.junit.Test;
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
public class ConsumerRepositoryTest {

    private Consumer consumer;

    @Autowired
    private ConsumerRepository consumerRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void beforeEach() {
        consumer = new Consumer();
        consumer.setUsername("luiso");
        consumer.setName("Luis Oliveira");
        consumer.setEmail("luis@ua.pt");
        consumer.setPassword("12345678");
        consumer.setAddress("Aveiro");
        consumer.setZipCode("3060-500");
        entityManager.persistAndFlush(consumer);
    }

    @Test
    public void whenGetById_returnConsumer() {
        Optional<Consumer> found = consumerRepository.findById(consumer.getUsername());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(consumer, found.get());
    }

    @Test
    public void whenFindAll_containsConsumer() {
        List<Consumer> found = consumerRepository.findAll();
        Assert.assertTrue(found.contains(consumer));
    }

    @Test
    public void failWhenGetById() {
        Optional<Consumer> found = consumerRepository.findById("test");
        Assert.assertFalse(found.isPresent());
    }

    @After
    public void afterEach() {
        entityManager.clear();
    }
}