package com.skillstorm.reliable_api.repositories;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.skillstorm.reliable_api.models.Category;

public interface CategoryRepo extends JpaRepository<Category, Long> {

    boolean existsByNameAllIgnoreCase(String name);
    List<Category> findAllByOrderByNameAsc();
    
}
