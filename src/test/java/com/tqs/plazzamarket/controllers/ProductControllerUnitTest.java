package com.tqs.plazzamarket.controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import com.tqs.plazzamarket.repositories.ProducerRepository;
import com.tqs.plazzamarket.repositories.ProductRepository;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerUnitTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private CategoryRepository categoryRepository;

    @MockBean
    private ProducerRepository producerRepository;

    private Category category;
    private Producer producer;
    private Product product;

    @Before
    public void beforeEach() {
        category = new Category("Flowers");

        producer = new Producer();
        producer.setUsername("luiso");

        product = new Product();
        product.setName("Potato");
        product.setQuantity(4);
        product.setPrice(5);
        product.setDescription("test");
        product.setProducer(producer);
        product.setCategory(category);

        // Mockito.when(product.getName()).thenReturn("Potato");
        // Mockito.when(product.getQuantity()).thenReturn(4.);
        // Mockito.when(product.getPrice()).thenReturn(5.);
        // Mockito.when(product.getDescription()).thenReturn("test");

        BDDMockito.given(productRepository.saveAndFlush(product)).willReturn(product);
        BDDMockito.given(productRepository.findAll()).willReturn(Arrays.asList(product));
        BDDMockito.given(categoryRepository.getOne(category.getName())).willReturn(category);
        BDDMockito.given(producerRepository.getOne(producer.getUsername())).willReturn(producer);
        BDDMockito.given(productRepository.findById(product.getId())).willReturn(Optional.of(product));
        BDDMockito.given(categoryRepository.findById(category.getName())).willReturn(Optional.of(category));
        BDDMockito.given(producerRepository.findById(producer.getUsername())).willReturn(Optional.of(producer));
    }

    @Test
    public void testCreateProduct() throws Exception {
        Map map = mapper.convertValue(product, Map.class);
        map.put("category", category.getName());

        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/products/add").contentType(MediaType.APPLICATION_JSON)
                        .sessionAttr("user", producer).content(mapper.writeValueAsString(map)))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn().getResponse().getContentAsString();

        Product actual = mapper.readValue(result, Product.class);
        actual.setProducer(producer); // Sets user since it is not included in json response
        actual.setCategory(category); // Sets user since it is not included in json response

        Assert.assertEquals(product, actual);
    }

    @Test
    public void testRemoveProductOk() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(String.format("/api/products/remove/%d", product.getId())).sessionAttr("user", producer)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testRemoveProductBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.delete(String.format("/api/products/remove/%d", product.getId() + 1)).sessionAttr("user", producer)
                .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testListAllOk() throws Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.get("/api/products").accept("application/json;charset=UTF-8")
                        .contentType(MediaType.APPLICATION_JSON).content(mapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        List list = mapper.readValue(result, List.class);
        Assert.assertEquals(1, list.size());

        Product actual = mapper.convertValue(list.get(0), Product.class);
        actual.setProducer(producer); // Sets user since it is not included in json response
        actual.setCategory(category); // Sets user since it is not included in json response

        Assert.assertEquals(product, actual);
    }

    @Test
    public void testListProducerProductsOk() throws Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.get(String.format("/api/products/%s", producer.getUsername()))
                        .accept("application/json;charset=UTF-8").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        List list = mapper.readValue(result, List.class);
        Assert.assertEquals(1, list.size());

        Product actual = mapper.convertValue(list.get(0), Product.class);
        actual.setProducer(producer); // Sets user since it is not included in json response
        actual.setCategory(category); // Sets user since it is not included in json response

        Assert.assertEquals(product, actual);
    }

    @Test
    public void testListProducerProductsBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format("/api/products/%s", producer.getUsername() + "P"))
                .accept("application/json;charset=UTF-8").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testSearchProductsCategoryOk() throws Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.get(String.format("/api/products/category/%s", category.getName()))
                        .accept("application/json;charset=UTF-8").contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(product)))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        List list = mapper.readValue(result, List.class);
        Assert.assertEquals(1, list.size());

        Product actual = mapper.convertValue(list.get(0), Product.class);
        actual.setProducer(producer); // Sets user since it is not included in json response
        actual.setCategory(category); // Sets user since it is not included in json response

        Assert.assertEquals(product, actual);
    }

    @Test
    public void testSearchProductsCategoryBadRequest() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get(String.format("/api/products/category/%s", category.getName() + "P"))
                .accept("application/json;charset=UTF-8").contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(product))).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
