package com.example.Spring.with.json.format.service;

import java.io.IOException;

public interface ProductService {
    void seedProductsDB() throws IOException;

    String findProductsInSpecificRange();

}
