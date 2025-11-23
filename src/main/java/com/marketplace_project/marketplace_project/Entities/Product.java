package com.marketplace_project.marketplace_project.Entities;

public class Product {
    private String id;
    private String name;
    private String description;
    private String category;
    private Float price;
    private Integer stock;
    private String status;
    private String user_id;

    public Product(){}

    public Product(String id, String name, String status, Integer stock, Float price, String category, String description, String user_id) {
        this.id = id;
        this.name = name;
        this.status = status;
        this.stock = stock;
        this.price = price;
        this.category = category;
        this.description = description;
        this.user_id = user_id;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
