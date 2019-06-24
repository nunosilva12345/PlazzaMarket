package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Category;
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
public class CategoryRepositoryTest {

    private Category category;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void beforeEach() {
        category = new Category("Bulbs");
        entityManager.persistAndFlush(category);
    }

    @Test
    public void whenGetById_returnCategory() {
        Optional<Category> found = categoryRepository.findById(category.getName());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(category, found.get());
    }

    @Test
    public void whenFindAll_containsConsumer() {
        List<Category> found = categoryRepository.findAll();
        Assert.assertTrue(found.contains(category));
    }

    @Test
    public void failWhenGetById() {
        Optional<Category> found = categoryRepository.findById("Flowers");
        Assert.assertFalse(found.isPresent());
    }

}
