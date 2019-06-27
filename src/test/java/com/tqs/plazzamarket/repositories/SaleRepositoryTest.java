package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Sale;
import com.tqs.plazzamarket.utils.Status;
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
public class SaleRepositoryTest {

    private Sale sale;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void beforeEach() {
        sale = new Sale();
        sale.setQuantity(4);
        sale.setStatus(Status.PROCESSING);
        entityManager.persistAndFlush(sale);
    }

    @Test
    public void whenGetById_returnCategory() {
        Optional<Sale> found = saleRepository.findById(sale.getId());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(sale, found.get());
    }

    @Test
    public void whenFindAll_containsConsumer() {
        List<Sale> found = saleRepository.findAll();
        Assert.assertTrue(found.contains(sale));
    }

    @Test
    public void failWhenGetById() {
        Optional<Sale> found = saleRepository.findById(4);
        Assert.assertFalse(found.isPresent());
    }

}
