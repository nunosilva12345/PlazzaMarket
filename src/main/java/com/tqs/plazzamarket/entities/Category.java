package com.tqs.plazzamarket.entities;

import java.util.List;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Category {
	@Id
	private String name;

	@OneToMany(mappedBy = "category")
	private List<Product> products;

	public Category(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}
}