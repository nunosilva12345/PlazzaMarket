package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;

import static org.hamcrest.Matchers.is;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class ProductControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProducerRepository producerRepository;

    private Producer producer;

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
        productRepository.deleteAll();
        categoryRepository.deleteAll();
        producerRepository.deleteAll();
        producerRepository.saveAndFlush(producer);
    }

    @Test
    @Transactional
    public void testCreateProduct() throws Exception {
        Map<String, Object> productJSON = new HashMap<>();
        productJSON.put("name", "Potato");
        productJSON.put("quantity", "4");
        productJSON.put("price", "5");
        productJSON.put("description", "test");

        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/products/add").contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("user", producer).content(mapper.writeValueAsString(productJSON)))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Product p = new Product();
        p.setName("Potato");
        p.setQuantity(4);
        p.setPrice(5);
        p.setDescription("test");
        p.setId(mapper.readValue(result, Product.class).getId());

        Assert.assertEquals(p, mapper.readValue(result, Product.class));

        Optional<Product> optional = productRepository.findById(mapper.readValue(result, Product.class).getId());
        Assert.assertTrue(optional.isPresent());
        
        Product actual = optional.get();
        Assert.assertEquals(p.getName(), actual.getName());
        Assert.assertTrue(p.getQuantity() == actual.getQuantity());
        Assert.assertEquals(p.getDescription(), actual.getDescription());
        Assert.assertTrue(p.getPrice() == actual.getPrice());
    }

    @Test
    public void testFindAll() throws Exception {
        Product p = new Product();
        p.setName("Potato");
        p.setQuantity(4);
        p.setPrice(5);
        p.setDescription("test");

        productRepository.saveAndFlush(p);

        mvc.perform(MockMvcRequestBuilders.get("/api/products/").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(MockMvcResultMatchers.jsonPath("$[0].name", is(p.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].quantity", is(p.getQuantity())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].price", is(p.getPrice())))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].description", is(p.getDescription())));
    }

    @Test
    @Transactional
    public void testRemoveProduct() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setQuantity(4);
        product.setPrice(5);
        product.setDescription("test");
        product.setName("Potato");
        product.setProducer(producer);
        product = productRepository.saveAndFlush(product);

        int size_beforeDelete = (int) productRepository.count();
        mvc.perform(MockMvcRequestBuilders.delete(String.format("/api/products/remove/%d", product.getId())).sessionAttr("user", producer))
                .andExpect(MockMvcResultMatchers.status().isOk());
        int size_atferDelete = (int) productRepository.count();

        Assert.assertEquals(size_beforeDelete - 1, size_atferDelete);

        Optional<Product> optional = productRepository.findById(product.getId());
        Assert.assertFalse(optional.isPresent());

    }

    @Test
    @Transactional
    public void testListProducerProducts() throws Exception {
        Producer producer = new Producer();
        producer.setUsername("nunosilva");
        producer.setName("Nuno Silva");
        producer.setEmail("nuno1@ua.pt");
        producer.setPassword("12345678");
        producer.setAddress("Porto");
        producer.setZipCode("4470-587");
        producer.setWebsite("https://www.olaadeus.pt");
        producer = producerRepository.saveAndFlush(producer);

        Category category = new Category("Flowers");
        categoryRepository.saveAndFlush(category);

        Product product = new Product();
        product.setQuantity(4);
        product.setPrice(5);
        product.setDescription("test");
        product.setName("Red Potato");
        product.setCategory(category);
        product.setProducer(producer);
        productRepository.saveAndFlush(product);

        Product product1 = new Product();
        product1.setQuantity(10);
        product1.setPrice(3);
        product1.setDescription("test too");
        product1.setName("Sweet Potato");
        product1.setProducer(producer);
        productRepository.saveAndFlush(product1);

        String responseList = mvc.perform(MockMvcRequestBuilders.get("/api/products/" + producer.getUsername()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        List<Object> list = mapper.readValue(responseList, List.class);

        Assert.assertEquals(2, list.size());

    }

    @Test
    @Transactional
    public void testSearchProductCategory() throws Exception {
        Category category = new Category("Flowers");
        categoryRepository.saveAndFlush(category);

        Product product = new Product();
        product.setQuantity(4);
        product.setPrice(5);
        product.setDescription("test");
        product.setName("Red Potato");
        product.setCategory(category);
        productRepository.saveAndFlush(product);

        Product product1 = new Product();
        product1.setQuantity(10);
        product1.setPrice(3);
        product1.setDescription("test too");
        product1.setName("Sweet Potato");
        product1.setCategory(category);
        productRepository.saveAndFlush(product1);

        categoryRepository.saveAndFlush(category);

        String responseList = mvc.perform(MockMvcRequestBuilders.get("/api/products/category/" + category.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        List list = mapper.readValue(responseList, List.class);

        Assert.assertEquals(2, list.size());
    }

}
