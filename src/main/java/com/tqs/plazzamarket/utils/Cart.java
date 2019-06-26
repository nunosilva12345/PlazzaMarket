package com.tqs.plazzamarket.utils;

import java.util.HashMap;
import java.util.Map;

import com.tqs.plazzamarket.entities.Product;

public class Cart {
    private Map<Product, Double> items = new HashMap<>();

    public Double add(Product product, Double quantity) {
        Double cartQuantity = items.get(product);
        quantity = cartQuantity != null ? cartQuantity + quantity : quantity;
        if (product.getQuantity() < quantity)
            return null;
        items.put(product, quantity);
        return quantity;
    }

    public Double get(Product product) {
        return items.get(product);
    }

    public int size() {
        return items.size();
    }
}