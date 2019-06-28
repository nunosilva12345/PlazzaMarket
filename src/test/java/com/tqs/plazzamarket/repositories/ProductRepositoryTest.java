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

import javax.transaction.Transactional;

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
        category = entityManager.persistAndFlush(category);
        product = new Product();
        product.setName("Potato");
        product.setQuantity(4.0);
        product.setPrice(5.0);
        product.setDescription("Test");
        product.setCategory(category);
        product = entityManager.persistAndFlush(product);
        System.out.println("INITIALIZING: " + product.getId());
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

    @Test
    @Transactional
    public void whenGetByID_removeProduct() {
        productRepository.deleteById(product.getId());
        Assert.assertFalse(productRepository.findById(product.getId()).isPresent());
    }

    @Test
    public void whenGetByNonID_dontRemove() {
        int size_beforeDelete = (int) productRepository.count();
        Optional<Product> found = productRepository.findById(500000000);
        if (found.isPresent())
            productRepository.deleteById(500000);
        int size_afterDelete = (int) productRepository.count();
        Assert.assertTrue(size_beforeDelete == size_afterDelete);
        Assert.assertEquals(size_beforeDelete, size_afterDelete);

    }

    @After
    public void afterEach() {
        productRepository.deleteAll();
    }
}
