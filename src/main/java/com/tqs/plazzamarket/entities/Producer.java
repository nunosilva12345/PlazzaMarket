package com.tqs.plazzamarket.entities;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;

import com.tqs.plazzamarket.utils.BaseUser;

import org.hibernate.validator.constraints.URL;

@Entity
public class Producer extends BaseUser {
    private static final long serialVersionUID = 5290073270256936397L;

    @URL
    @NotNull(message = "Website is mandatory")
    private String website;

    @OneToMany(mappedBy = "producer")
    private List<Product> products = new ArrayList<>();

    public String getWebsite() {
        return this.website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(Product products) {
        this.products.add(products);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((products == null) ? 0 : products.hashCode());
        result = prime * result + ((website == null) ? 0 : website.hashCode());
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
        Producer other = (Producer) obj;
        if (products == null) {
            if (other.products != null)
                return false;
        } else if (!products.equals(other.products))
            return false;
        if (website == null) {
            if (other.website != null)
                return false;
        } else if (!website.equals(other.website))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return super.getUsername();
    }
}
