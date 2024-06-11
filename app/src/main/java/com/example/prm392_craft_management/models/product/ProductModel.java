package com.example.prm392_craft_management.models.product;

import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.image.ImageModel;

import java.util.List;

public class ProductModel {
    private int id, status, discount;
    private String name, description;
    private double price, weight, cost;
    private List<ImageModel> images;
    private List<FestivalModel> festivals;

    public ProductModel(int id, int status, int discount, String name, String description, double price, double weight, double cost, List<ImageModel> images, List<FestivalModel> festivals) {
        this.id = id;
        this.status = status;
        this.discount = discount;
        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.cost = cost;
        this.images = images;
        this.festivals = festivals;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public List<ImageModel> getImages() {
        return images;
    }

    public void setImages(List<ImageModel> images) {
        this.images = images;
    }

    public List<FestivalModel> getFestivals() {
        return festivals;
    }

    public void setFestivals(List<FestivalModel> festivals) {
        this.festivals = festivals;
    }
}
