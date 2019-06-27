package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Sale;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaleRepository extends JpaRepository<Sale, Integer> {
}
