package ua.tqs.g6.project.entities;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ProductName {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String prodName;
    
    @OneToMany(
        mappedBy = "productName",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<Product> products;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProductName() {
        return prodName;
    }

    public void setProductName(String prodName) {
        this.prodName = prodName;
    }
    
    
}
