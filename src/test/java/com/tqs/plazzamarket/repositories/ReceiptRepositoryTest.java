package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Receipt;
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
public class ReceiptRepositoryTest {

    private Receipt receipt;

    @Autowired
    private ReceiptRepository receiptRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void beforeEach() {
        receipt = new Receipt();
        receipt.setPrice(4.0);
        entityManager.persistAndFlush(receipt);
    }

    @Test
    public void whenGetById_returnReceipt() {
        Optional<Receipt> found = receiptRepository.findById(receipt.getId());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(receipt, found.get());
    }

    @Test
    public void whenFindAll_containsReceipt() {
        List<Receipt> found = receiptRepository.findAll();
        Assert.assertTrue(found.contains(receipt));
    }

    @Test
    public void failWhenGetById() {
        Optional<Receipt> found = receiptRepository.findById(3);
        Assert.assertFalse(found.isPresent());
    }
}
