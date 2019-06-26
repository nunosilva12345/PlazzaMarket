package com.tqs.plazzamarket.utils;

import java.util.HashMap;
import java.util.Map;

import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;

public class Cart {
    private Map<Integer, Double[]> items = new HashMap<>();

    private double total = 0;

    public Double add(Product product, Double quantity) {
        //Double cartQuantity = items.get(product.getId())[0];
        //quantity = cartQuantity != null ? cartQuantity + quantity : quantity;
        if (product.getQuantity() < quantity)
            return null;
        double totalCompra = product.getPrice()*quantity;
        Double[] pair = new Double[2];
        pair[0] = quantity;
        pair[1] = totalCompra;
        items.put(product.getId(), pair);
        total += product.getPrice()*quantity;
        return quantity;
    }

    public double getTotal() {
        return this.total;
    }

    public Double get(Product product) {
        return items.get(product.getId())[0];
    }

    public int size() {
        return items.size();
    }

    public Map<Integer, Double[]> getItems() {
        return this.items;
    }
}