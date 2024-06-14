package com.example.prm392_craft_management.models.order;

public class OrderModel {
    private int image;
    private String name;
    private String status_order;

    public OrderModel(int image, String name, String status_order) {
        this.image = image;
        this.name = name;
        this.status_order = status_order;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus_order() {
        return status_order;
    }

    public void setStatus_order(String status_order) {
        this.status_order = status_order;
    }
}
