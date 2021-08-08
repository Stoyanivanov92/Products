package com.example.Spring.with.json.format.service;


import com.example.Spring.with.json.format.model.dto.ProductDto;
import com.example.Spring.with.json.format.model.dto.ProductsInRangeDto;
import com.example.Spring.with.json.format.model.entity.Product;
import com.example.Spring.with.json.format.repository.ProductRepository;
import com.example.Spring.with.json.format.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final String FILE_PATH_PRODUCTS = "src/main/resources/files/products.json";

    private final ProductRepository productRepository;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;
    private final Gson gson;
    private final UserService userService;
    private final CategoryService categoryService;

    public ProductServiceImpl(ProductRepository productRepository, ValidationUtil validationUtil, ModelMapper modelMapper, Gson gson, UserService userService, CategoryService categoryService) {
        this.productRepository = productRepository;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
        this.gson = gson;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @Override
    public void seedProductsDB() throws IOException {
        if (productRepository.count() > 0) {
            return;
        }
        String jsonProducts = Files.readString(Path.of(FILE_PATH_PRODUCTS));
        ProductDto[] productDtos = gson.fromJson(jsonProducts, ProductDto[].class);
        for (ProductDto productDto : productDtos) {
            if (validationUtil.violations(productDto).isEmpty()) {
                Product product = modelMapper.map(productDto, Product.class);
                product.setSeller(userService.findUser());
                if (product.getPrice().compareTo(BigDecimal.valueOf(700)) < 0) {
                    product.setBuyer(userService.findUser());
                }
                product.setCategories(categoryService.findCategories());
                productRepository.save(product);
            }
        }
    }

    @Override
    public String findProductsInSpecificRange() {
        List<Product> products = productRepository.findAll();
        List<Product> newProducts = new ArrayList<>();
        for (Product product : products) {
            if (product.getPrice().compareTo(BigDecimal.valueOf(500)) >= 0 && product.getPrice().compareTo(BigDecimal.valueOf(1000)) <= 0) {
                if (product.getBuyer() == null) {
                    newProducts.add(product);
                }
            }
        }
        List<Product> orderedProducts = newProducts
                .stream()
                .sorted((p1, p2) -> p1.getPrice().compareTo(p2.getPrice()))
                .collect(Collectors.toList());
        List<ProductsInRangeDto> productsInRangeDtos = new ArrayList<>();
        for (Product product : orderedProducts) {
            ProductsInRangeDto productsInRangeDto = modelMapper.map(product, ProductsInRangeDto.class);
            productsInRangeDto.setSeller(product.getSeller().getFirstName() + " " + product.getSeller().getLastName());
            productsInRangeDtos.add(productsInRangeDto);
        }
        return gson.toJson(productsInRangeDtos);

    }


}
