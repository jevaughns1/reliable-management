package com.skillstorm.reliable_api.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.skillstorm.reliable_api.models.Product;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    Optional<Product>  findByPublicIdAndIsDeletedFalse(String publicId);
    
    List<Product>  findAllByIsDeletedFalse();
}
