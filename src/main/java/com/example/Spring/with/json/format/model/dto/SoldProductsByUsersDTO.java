package com.example.Spring.with.json.format.model.dto;

import com.example.Spring.with.json.format.model.entity.Product;
import com.google.gson.annotations.Expose;

import java.util.Set;

public class SoldProductsByUsersDTO {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private SoldProductsDto[] soldProducts;

    public SoldProductsByUsersDTO() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public SoldProductsDto[] getSoldProducts() {
        return soldProducts;
    }

    public void setSoldProducts(SoldProductsDto[] soldProducts) {
        this.soldProducts = soldProducts;
    }
}
