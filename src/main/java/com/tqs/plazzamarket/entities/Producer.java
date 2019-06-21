package com.tqs.plazzamarket.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;

import com.tqs.plazzamarket.utils.BaseUser;

@Entity
public class Producer extends BaseUser {
    private String website;

    @OneToMany(mappedBy = "producer")
    private List<Product> products;

	public String getWebsite() {
		return this.website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}
