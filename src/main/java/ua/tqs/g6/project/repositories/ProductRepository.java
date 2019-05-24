/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.tqs.g6.project.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ua.tqs.g6.project.entities.Product;
import ua.tqs.g6.project.entities.ProductName;

public interface ProductRepository extends JpaRepository<Product, Integer>
{
	@Query("SELECT u FROM Product u WHERE u.productName IN ?1")
    public Iterable<Product> findAll(Iterable<ProductName> productNames);
}
