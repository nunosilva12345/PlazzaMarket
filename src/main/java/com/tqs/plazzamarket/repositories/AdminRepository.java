package com.tqs.plazzamarket.repositories;

import com.tqs.plazzamarket.entities.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository  extends JpaRepository<Admin, String> {
}
