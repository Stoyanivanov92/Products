package com.example.Spring.with.json.format.model.dto;

import com.google.gson.annotations.Expose;

import javax.validation.constraints.Size;

public class CategoryDto {
    @Expose
    private String name;

    public CategoryDto() {
    }

    @Size(min = 3, max = 15)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
