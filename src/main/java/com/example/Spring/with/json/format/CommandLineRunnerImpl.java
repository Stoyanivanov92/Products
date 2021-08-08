package com.example.Spring.with.json.format;

import com.example.Spring.with.json.format.service.CategoryService;
import com.example.Spring.with.json.format.service.ProductService;
import com.example.Spring.with.json.format.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {
    private final UserService userService;
    private final CategoryService categoryService;
    private final ProductService productService;
    private final Scanner scan;

    public CommandLineRunnerImpl(UserService userService, CategoryService categoryService, ProductService productService, Scanner scan) {
        this.userService = userService;
        this.categoryService = categoryService;
        this.productService = productService;
        this.scan = scan;
    }

    @Override
    public void run(String... args) throws Exception {
        userService.seedUsersDB();
        categoryService.seedCategoriesDB();
        productService.seedProductsDB();

        System.out.println("Enter number");
        int number = Integer.parseInt(scan.nextLine());
        if (number == 1) {
            exportProductsInRange();
        } else if (number == 2) {
            exportAllUsersWithAtLeastOneItemSoldWithBuyer();
        } else if (number == 3) {
            exportAllCategoriesOrderedByNumberProducts();
        }
    }

    private void exportAllCategoriesOrderedByNumberProducts() throws IOException {
        String categoriesByProducts = categoryService.getAllCategoriesOrderedByNumberProducts();
        Files.writeString(Path.of("src/main/resources/files/out/categories by products.json"), categoriesByProducts);
    }

    private void exportAllUsersWithAtLeastOneItemSoldWithBuyer() throws IOException {
       String usersWithSoldItems = userService.getAllUsersWithAtLeastOneItemSoldWithBuyer();
       Files.writeString(Path.of("src/main/resources/files/out/users with sold products.json"), usersWithSoldItems);
    }

    private void exportProductsInRange() throws IOException {
        String productsInSpecificRange = productService.findProductsInSpecificRange();
        Files.writeString(Path.of("src/main/resources/files/out/products in range.json"), productsInSpecificRange);
    }
}
