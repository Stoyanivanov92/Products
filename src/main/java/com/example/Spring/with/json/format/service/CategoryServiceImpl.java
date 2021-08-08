package com.example.Spring.with.json.format.service;

import com.example.Spring.with.json.format.model.dto.CategoriesByProductsDto;
import com.example.Spring.with.json.format.model.dto.CategoryDto;
import com.example.Spring.with.json.format.model.entity.Category;
import com.example.Spring.with.json.format.repository.CategoryRepository;
import com.example.Spring.with.json.format.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final String FILE_PATH_CATEGORIES = "src/main/resources/files/categories.json";

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public void seedCategoriesDB() throws IOException {
        if (categoryRepository.count() > 0) {
            return;
        }
        String jsonCategories = Files.readString(Path.of(FILE_PATH_CATEGORIES));
        CategoryDto[] categoryDtos = gson.fromJson(jsonCategories, CategoryDto[].class);
        for (CategoryDto categoryDto : categoryDtos) {
            if (validationUtil.violations(categoryDto).isEmpty()) {
                Category category = modelMapper.map(categoryDto, Category.class);
                categoryRepository.save(category);
            }
        }
    }

    @Override
    public Set<Category> findCategories() {
        Set<Category> categorySet = new HashSet<>();
        int categoryCount = ThreadLocalRandom.current().nextInt(1, 3);

        for (int i = 0; i < categoryCount; i++) {
            long id = ThreadLocalRandom.current().nextLong(1, categoryRepository.count() + 1);
            categorySet.add(categoryRepository.findById(id).orElse(null));
        }
        return categorySet;
    }

    @Override
    public String getAllCategoriesOrderedByNumberProducts() {
        List<CategoriesByProductsDto> categoriesByProductsDtos = new ArrayList<>();
        List<Category> categories = categoryRepository.findAll()
                .stream()
                .sorted((c1, c2) -> c2.getProducts().size() - c1.getProducts().size())
                .collect(Collectors.toList());
        for (Category category : categories) {
            CategoriesByProductsDto categoriesByProductsDto = modelMapper.map(category, CategoriesByProductsDto.class);
            categoriesByProductsDto.setCategory(category.getName());
            categoriesByProductsDto.setProductsCount(category.getProducts().size());
            categoriesByProductsDto.setAveragePrice(BigDecimal.valueOf(category.getProducts().stream().mapToDouble(e -> e.getPrice().doubleValue()).average().orElse(0)));
            categoriesByProductsDto.setTotalRevenue(BigDecimal.valueOf(category.getProducts().stream().mapToDouble(e -> e.getPrice().doubleValue()).sum()));
            categoriesByProductsDtos.add(categoriesByProductsDto);
        }
        return gson.toJson(categoriesByProductsDtos);

    }
}
