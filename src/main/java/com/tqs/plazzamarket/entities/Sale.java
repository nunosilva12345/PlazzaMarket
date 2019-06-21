package com.tqs.plazzamarket.entities;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.OneToOne;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.CascadeType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

import com.tqs.plazzamarket.utils.Status;

@Entity
public class Sale {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "consumer_username")
	private Consumer consumer;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "receipt_id")
	private Receipt receipt;

	private double quantity;

	@Enumerated(EnumType.ORDINAL)
	private Status status;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public Consumer getConsumer()
	{
		return consumer;
	}

	public void setConsumer(Consumer consumer)
	{
		this.consumer = consumer;
	}

	public Receipt getReceipt()
	{
		return receipt;
	}

	public void setReceipt(Receipt receipt)
	{
		this.receipt = receipt;
	}

	public double getQuantity()
	{
		return quantity;
	}

	public void setQuantity(double quantity)
	{
		this.quantity = quantity;
	}

	public Status getStatus()
	{
		return status;
	}

	public void setStatus(Status status)
	{
		this.status = status;
	}
}
