package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Producer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProducerRepository extends JpaRepository<Producer, String> {
    
}