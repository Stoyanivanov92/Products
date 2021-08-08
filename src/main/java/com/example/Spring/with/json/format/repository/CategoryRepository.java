package com.example.Spring.with.json.format.repository;

import com.example.Spring.with.json.format.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

}
