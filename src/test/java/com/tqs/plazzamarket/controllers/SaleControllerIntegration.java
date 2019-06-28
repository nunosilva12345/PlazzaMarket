package com.tqs.plazzamarket.controllers;

import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.entities.Sale;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.repositories.SaleRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class SaleControllerIntegration {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    private Producer producer;

    private Sale sale;

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
        Producer p = producerRepository.saveAndFlush(producer);

        Double quantity = 4.;
        Integer productId = 1;

        product = new Product();
        product.setId(productId);
        product.setQuantity(quantity);
        product.setPrice(5);
        product.setProducer(producer);
        product.setDescription("test");
        product.setName("Potato");
        product = productRepository.saveAndFlush(product);

        sale = new Sale();
        sale.setProduct(product);
        sale.setQuantity(4);
        sale = saleRepository.saveAndFlush(sale);

        producer.setProducts(p.getProducts());
    }

    @Test
    public void testAcept() throws Exception{
        mvc
                .perform(MockMvcRequestBuilders.get(String.format("/api/sale/accept/%d", sale.getId()))
                        .sessionAttr("user", producer))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void testReject() throws Exception {
        mvc
                .perform(MockMvcRequestBuilders.get(String.format("/api/sale/reject/%d", sale.getId()))
                        .sessionAttr("user", producer))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

    }

}
