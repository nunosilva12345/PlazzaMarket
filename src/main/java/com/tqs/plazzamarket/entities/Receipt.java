package com.tqs.plazzamarket.entities;

import javax.persistence.Id;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

@Entity
public class Receipt implements Serializable {
	private static final long serialVersionUID = 7491573502632013934L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private double price;

	@OneToOne(mappedBy = "receipt")
	private Sale sale;

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		long temp;
		temp = Double.doubleToLongBits(price);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((sale == null) ? 0 : sale.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Receipt receipt = (Receipt) obj;
		return id == receipt.id && price == receipt.price
				&& Objects.equals(sale, receipt.sale);
	}
}