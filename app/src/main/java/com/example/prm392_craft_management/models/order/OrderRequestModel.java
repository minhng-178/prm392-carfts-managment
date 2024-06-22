package com.example.prm392_craft_management.models.order;

import java.util.List;

public class OrderRequestModel {
    private int user_id;
    private String phone;
    private String address;
    private int distance;
    private List<Integer> carts;
    private String note;

    public OrderRequestModel(String address, List<Integer> carts, int distance, String note, String phone, int user_id) {
        this.address = address;
        this.carts = carts;
        this.distance = distance;
        this.note = note;
        this.phone = phone;
        this.user_id = user_id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<Integer> getCarts() {
        return carts;
    }

    public void setCarts(List<Integer> carts) {
        this.carts = carts;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
