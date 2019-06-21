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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((sales == null) ? 0 : sales.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        Consumer other = (Consumer) obj;
        if (sales == null) {
            if (other.sales != null)
                return false;
        } else if (!sales.equals(other.sales))
            return false;
        return true;
    }
}
