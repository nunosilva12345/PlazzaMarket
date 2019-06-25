package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Product;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    
    List<Product> findAllById(String producer_id);
}
