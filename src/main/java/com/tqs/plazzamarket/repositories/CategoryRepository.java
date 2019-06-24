package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, String> {
}
