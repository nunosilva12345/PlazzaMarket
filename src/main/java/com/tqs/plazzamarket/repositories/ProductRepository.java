package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
}
