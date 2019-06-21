package com.tqs.plazzamarket.entities;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import javax.persistence.CascadeType;

import com.tqs.plazzamarket.utils.BaseUser;

@Entity
public class Consumer extends BaseUser {
    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales = new ArrayList<>();

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}
