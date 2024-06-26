package com.example.prm392_craft_management.models.cart;


import java.util.List;

public class CartResponseModel {
    private int user_id;
    private int total_items;
    private int page;
    private int size;
    private int total_pages;
    private List<CartModel> cart;

    public CartResponseModel(int user_id, int total_items, int page, int size, int total_pages, List<CartModel> cart) {
        this.user_id = user_id;
        this.total_items = total_items;
        this.page = page;
        this.size = size;
        this.total_pages = total_pages;
        this.cart = cart;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTotal_items() {
        return total_items;
    }

    public void setTotal_items(int total_items) {
        this.total_items = total_items;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public List<CartModel> getCart() {
        return cart;
    }

    public void setCart(List<CartModel> cart) {
        this.cart = cart;
    }
}
