package com.example.Spring.with.json.format.repository;

import com.example.Spring.with.json.format.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
