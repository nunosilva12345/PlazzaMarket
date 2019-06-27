package com.tqs.plazzamarket.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tqs.plazzamarket.entities.Consumer;
import com.tqs.plazzamarket.entities.Product;
import com.tqs.plazzamarket.repositories.ProductRepository;
import com.tqs.plazzamarket.utils.Cart;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@RunWith(SpringRunner.class)
@WebMvcTest(CartController.class)
public class CartControllerUnitTest {
    @Autowired
    private MockMvc mvc;


    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private ProductRepository productRepository;

    @Mock
    private Consumer consumer;

    @Mock
    private Product product;

    @Mock
    private Cart cart;

    private Map<String, Object> map;

    @Before
    public void beforeEach() {
        map = new HashMap<>();
        Double quantity = 4.;
        Integer productId = 1;
        map.put("quantity", quantity);
        map.put("productId", productId);
        Mockito.when(product.getId()).thenReturn(productId);
        BDDMockito.when(cart.add(product, quantity)).thenReturn(quantity);
        BDDMockito.when(cart.removeProduct(productId)).thenReturn(true);
        BDDMockito.given(productRepository.findById(productId)).willReturn(Optional.of(product));
    }

    @Test
    public void testAddSuccess() throws Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.post("/api/cart/add").contentType(MediaType.APPLICATION_JSON).accept("application/json;charset=UTF-8")
                        .content(mapper.writeValueAsString(map)).sessionAttr("cart", cart).sessionAttr("user", consumer))
                .andExpect(MockMvcResultMatchers.status().isCreated()).andReturn()
                .getResponse().getContentAsString();
        // Map obj = mapper.readValue(result, Map.class);
        Assert.assertEquals(map.get("quantity"), Double.parseDouble(result));
    }

    @Test
    public void testClearSuccess() throws Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.delete("/api/cart/clear").sessionAttr("cart", cart).sessionAttr("user", consumer))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();

        Assert.assertTrue(Integer.valueOf(result) == 0);
    }

    @Test
    public void testRemoveProduct() throws Exception {
        String result = mvc
                .perform(MockMvcRequestBuilders.delete("/api/cart/remove/" + product.getId())
                        .content(mapper.writeValueAsString(map)).sessionAttr("cart", cart).sessionAttr("user", consumer))
                .andExpect(MockMvcResultMatchers.status().isOk()).andReturn()
                .getResponse().getContentAsString();
        Assert.assertEquals(product.getId(), Integer.parseInt(result));
    }
}
