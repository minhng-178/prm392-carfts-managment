package com.example.prm392_craft_management.models.order;

import com.example.prm392_craft_management.models.product.ProductModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderProductsModel {
    private int id;
    private int userId;
    private String phone;
    private String address;
    private int distance;
    @SerializedName("total_product_price")
    private int totalProductPrice;
    @SerializedName("shipping_fee")
    private int shippingFee;
    @SerializedName("total_price")
    private int totalPrice;
    private int status;
    private String username;
    @SerializedName("is_admin_confirm")
    private boolean isAdminConfirm;

    @SerializedName("created_at")
    private String createdAt;
    private List<ProductModel> products;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public void setProducts(List<ProductModel> products) {
        this.products = products;
    }

    public int getShippingFee() {
        return shippingFee;
    }

    public void setShippingFee(int shippingFee) {
        this.shippingFee = shippingFee;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalProductPrice() {
        return totalProductPrice;
    }

    public void setTotalProductPrice(int totalProductPrice) {
        this.totalProductPrice = totalProductPrice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isAdminConfirm() {
        return isAdminConfirm;
    }

    public void setAdminConfirm(boolean adminConfirm) {
        isAdminConfirm = adminConfirm;
    }
}
