package com.example.Spring.with.json.format.service;

import com.example.Spring.with.json.format.model.entity.Category;

import java.io.IOException;
import java.util.Set;

public interface CategoryService {
    void seedCategoriesDB() throws IOException;

    Set<Category> findCategories();

    String getAllCategoriesOrderedByNumberProducts();

}
