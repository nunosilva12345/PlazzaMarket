/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.tqs.g6.project.Entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

@Entity
public class Producer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String type;
    private String website;
    
    @OneToMany(
        mappedBy = "producer",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Product> products;
    
    @ManyToMany(mappedBy = "following")
    private List<Consumer> followers;
    
    @OneToMany(
        mappedBy = "producer",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Sales> sales;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getWebsite() {
        return website;
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

    public List<Consumer> getFollowers() {
        return followers;
    }

    public void setFollowers(List<Consumer> followers) {
        this.followers = followers;
    }

    public List<Sales> getSales() {
        return sales;
    }

    public void setSales(List<Sales> sales) {
        this.sales = sales;
    }
    
    
    
}
