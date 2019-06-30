package com.tqs.plazzamarket.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Admin;
import com.tqs.plazzamarket.entities.Category;
import com.tqs.plazzamarket.repositories.CategoryRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RunWith(SpringRunner.class)
@WebMvcTest(CategoryController.class)
public class CategoryControllerUnitTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CategoryRepository categoryRepository;

    private Category category;

    private List<Category> categories;

    String categoryName;

    @Before
    public void beforeEach() {
        categoryName = "Flowers";
        category = new Category(categoryName);
        categories = new ArrayList<>();
        categories.add(category);
        BDDMockito.given(categoryRepository.findById(categoryName)).willReturn(Optional.of(category));
        BDDMockito.given(categoryRepository.findAll()).willReturn(categories);
        BDDMockito.given(categoryRepository.saveAndFlush(category)).willReturn(category);
    }

    @Test
    public void createCategoryTest() throws Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/category/addcategory").contentType(MediaType.APPLICATION_JSON).sessionAttr("user", new Admin())
                        .content(mapper.writeValueAsString(category)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        Assert.assertEquals(category, mapper.readValue(result, Category.class));
    }

    @Test
    public void findAllTest() throws Exception{
        String response = mvc
                .perform(MockMvcRequestBuilders.get("/api/category/"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
    }

    @Test
    public void removeCategoryTest() throws Exception {
        String response = mvc
                .perform(MockMvcRequestBuilders.delete("/api/category/delete/" + categoryName).sessionAttr("user", new Admin()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        Assert.assertEquals("true", response);
    }
}
