package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Consumer;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ConsumerRepository extends JpaRepository<Consumer, String> {
    
}