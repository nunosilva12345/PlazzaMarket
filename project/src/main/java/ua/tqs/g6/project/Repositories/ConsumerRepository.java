/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.tqs.g6.project.Repositories;

import ua.tqs.g6.project.Entities.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import ua.tqs.g6.project.Entities.Category;


public interface ConsumerRepository extends JpaRepository<Consumer, Integer> {
    
}
