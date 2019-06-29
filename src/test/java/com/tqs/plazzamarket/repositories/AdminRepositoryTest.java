package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Admin;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@DataJpaTest
@TestPropertySource(locations = "classpath:test.properties")
public class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private EntityManager entityManager;

    private Admin admin;

    @Before
    public void beforeEach() {
        admin = new Admin();
        admin.setPassword("admin");
        admin.setUsername("admin");
        admin = adminRepository.saveAndFlush(admin);
    }

    @Test
    public void whenGetById_returnConsumer() {
        Optional<Admin> found = adminRepository.findById(admin.getUsername());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(admin, found.get());
    }

    @Test
    public void whenFindAll_containsConsumer() {
        List<Admin> found = adminRepository.findAll();
        Assert.assertTrue(found.contains(admin));
    }

    @Test
    public void failWhenGetById() {
        Optional<Admin> found = adminRepository.findById("test");
        Assert.assertFalse(found.isPresent());
    }

    @After
    public void afterEach() {
        entityManager.clear();
    }
}
