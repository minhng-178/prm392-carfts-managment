package com.example.prm392_craft_management.models.cart;

import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.image.ImageModel;

import java.util.List;

public class CartModel {
    private int cart_id, product_id, amount, product_amount, status, discount, user_id;
    private String name, description;
    private double price;
    private List<ImageModel> images;
    private List<FestivalModel> festivals;
    private List<CartProduct> products;

    public CartModel(int cart_id, int product_id, int amount, int product_amount, int status, int discount, String name, String description, double price, List<ImageModel> images, List<FestivalModel> festivals) {
        this.cart_id = cart_id;
        this.product_id = product_id;
        this.amount = amount;
        this.product_amount = product_amount;
        this.status = status;
        this.discount = discount;
        this.name = name;
        this.description = description;
        this.price = price;
        this.images = images;
        this.festivals = festivals;
    }

    public CartModel(int user_id, List<CartProduct> products) {
        this.user_id = user_id;
        this.products = products;
    }

    public static class CartProduct {
        private int product_id;
        private int amount;

        public CartProduct(int product_id, int amount) {
            this.product_id = product_id;
            this.amount = amount;
        }

        public int getProduct_id() {
            return product_id;
        }

        public void setProduct_id(int product_id) {
            this.product_id = product_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public List<CartProduct> getProducts() {
        return products;
    }

    public void setProducts(List<CartProduct> products) {
        this.products = products;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getProduct_amount() {
        return product_amount;
    }

    public void setProduct_amount(int product_amount) {
        this.product_amount = product_amount;
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
