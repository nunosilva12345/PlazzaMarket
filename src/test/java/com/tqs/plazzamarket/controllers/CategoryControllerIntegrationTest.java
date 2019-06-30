package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Admin;
import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "classpath:test.properties")
public class CategoryControllerIntegrationTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;

    @Before
    public void beforeEach() {
        categoryRepository.deleteAll();
    }

    @Test
    public void createCategoryTest() throws Exception {
        category = new Category("Flowers");
        String result = mvc
                .perform(
                        MockMvcRequestBuilders.post("/api/category/addcategory").contentType(MediaType.APPLICATION_JSON)
                                .sessionAttr("user", new Admin()).content(mapper.writeValueAsString(category)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Assert.assertEquals(category, mapper.readValue(result, Category.class));
    }

    @Test
    public void findAllTest() throws Exception {
        category = new Category("Flowers");
        categoryRepository.saveAndFlush(category);
        String response = mvc.perform(MockMvcRequestBuilders.get("/api/category/"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
    }

    @Test
    public void removeCategoryTest() throws Exception {
        category = new Category("Flowers");
        categoryRepository.saveAndFlush(category);
        String response = mvc
                .perform(MockMvcRequestBuilders.delete("/api/category/delete/Flowers").sessionAttr("user", new Admin()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn().getResponse().getContentAsString();
        Assert.assertEquals("true", response);
    }

}
