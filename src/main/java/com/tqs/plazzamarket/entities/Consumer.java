package com.tqs.plazzamarket.entities;

import java.util.List;
import java.util.ArrayList;

import javax.persistence.Id;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.CascadeType;

@Entity
public class Consumer {
    @Id
    private String username;
    private String name;
    private String email;
    private String password;
    private String address;
    private String zipCode;

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public List<Sale> getSales() {
        return sales;
    }

    public void setSales(List<Sale> sales) {
        this.sales = sales;
    }
}
