package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Producer;
import com.tqs.plazzamarket.entities.Product;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, String> {
        
}