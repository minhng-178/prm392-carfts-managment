package com.example.prm392_craft_management.models.product;


import java.util.List;

public class ProductResponseModel {
    private int total;
    private int page;
    private int size;
    private int total_pages;
    private List<ProductModel> data;

    public ProductResponseModel(int total, int page, int size, int total_pages, List<ProductModel> data) {
        this.total = total;
        this.page = page;
        this.size = size;
        this.total_pages = total_pages;
        this.data = data;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
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

    public List<ProductModel> getData() {
        return data;
    }

    public void setData(List<ProductModel> data) {
        this.data = data;
    }
}
