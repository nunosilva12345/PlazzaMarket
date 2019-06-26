package com.tqs.plazzamarket.utils;

import com.tqs.plazzamarket.entities.Product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CartUnitTest {
    private Cart cart;

    @Mock
    private Product product;

    private int productId = 1;

    @Before
    public void beforeEach() {
        cart = new Cart();
        Mockito.when(product.getId()).thenReturn(productId);
        Mockito.when(product.getQuantity()).thenReturn(5.);
    }

    @Test
    public void testAddSuccess() {
        final Double quantity = 4.;
        Assert.assertEquals(quantity, cart.add(product, quantity));
        Assert.assertEquals(1, cart.size());
        Assert.assertEquals(quantity, cart.get(product));
    }

    @Test
    public void testAddFail() {
        final Double quantity = 6.;
        Assert.assertEquals(null, cart.add(product, quantity));
        Assert.assertEquals(0, cart.size());
    }

    @Test
    public void testSize() {
        final Double quantity = 4.;
        Assert.assertEquals(quantity, cart.add(product, quantity));
        Assert.assertEquals(1, cart.size());
    }

    @Test
    public void testRemoveProductSuccess() {
        final Double quantity = 4.;
        cart.add(product, quantity);
        Assert.assertEquals(true, cart.removeProduct(productId));
    }

    @Test
    public void testRemoveProductFail() {
        final Double quantity = 4.;
        cart.add(product, quantity);
        Assert.assertEquals(false, cart.removeProduct(2));
    }


}