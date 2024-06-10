package com.example.prm392_craft_management.models.festival;

import java.util.List;

public class FestivalResponseModel {
    private int total;
    private int page;
    private int size;
    private int total_pages;
    private List<FestivalModel> data;

    public FestivalResponseModel(int total, int page, int size, int total_pages, List<FestivalModel> data) {
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

    public List<FestivalModel> getData() {
        return data;
    }

    public void setData(List<FestivalModel> data) {
        this.data = data;
    }
}
