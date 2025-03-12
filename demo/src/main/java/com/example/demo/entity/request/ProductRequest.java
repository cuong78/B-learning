package com.example.demo.entity.request;

import com.example.demo.entity.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
public class ProductRequest {
    public long id = 0;

    @NotBlank
    public String name;

    @Min(value = 0)
    public float price;

    @Min(value = 0)
    public int quantity;

    @NotBlank
    public String image;

    //PD00001
    @Pattern(regexp = "PD\\d{5}", message = "Code must be PDxxxxx!")
    @Column(unique = true)
    public String code;

    @NotNull
    public long categoryId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }
}
