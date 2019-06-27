package com.tqs.plazzamarket.utils;

import java.util.HashMap;
import java.util.Map;

import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;

public class Cart {
    private Map<Integer, Double[]> items = new HashMap<>();

    private double total = 0;

    public Double add(Product product, Double quantity) {
        if (product.getQuantity() < quantity)
            return null;
        double totalCompra = product.getPrice() * quantity;
        items.put(product.getId(), new Double[] { quantity, totalCompra });
        total += totalCompra;
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
    
    public void clearList(){
        items.clear();
        total = 0;
    }

    public boolean removeProduct(int productId) {
        if (items.containsKey(productId)) {
            total -= items.get(productId)[1];
            items.remove(productId);
            return true;
        }
        return false;
    }

}