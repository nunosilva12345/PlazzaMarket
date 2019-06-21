package com.tqs.plazzamarket.entities;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Receipt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private double price;

    @OneToOne(mappedBy = "receipt")
    private Sale sale;

    public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Product getProduct() {
		return this.product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public double getPrice() {
		return this.price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Sale getSale() {
		return this.sale;
	}

	public void setSale(Sale sale) {
		this.sale = sale;
	}
}