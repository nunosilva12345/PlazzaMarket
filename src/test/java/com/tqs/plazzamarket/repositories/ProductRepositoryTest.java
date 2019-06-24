package com.tqs.plazzamarket.repositories;


import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Product;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:test.properties")
public class ProductRepositoryTest {

    private Product product;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void beforeEach() {
        Category category = new Category("Bulbs");
        entityManager.persistAndFlush(category);
        product = new Product();
        product.setName("Potato");
        product.setQuantity(4.0);
        product.setPrice(5.0);
        product.setDescription("Test");
        product.setCategory(category);
        entityManager.persistAndFlush(product);
    }

    @Test
    public void whenGetById_returnProduct() {
        Optional<Product> found = productRepository.findById(product.getId());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(product, found.get());
    }

    @Test
    public void whenFindAll_containsConsumer() {
        List<Product> found = productRepository.findAll();
        Assert.assertTrue(found.contains(product));
    }

    @After
    public void afterEach() {
        entityManager.clear();
    }
}
