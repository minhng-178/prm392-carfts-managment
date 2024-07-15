package com.example.prm392_craft_management.models.order;

import com.example.prm392_craft_management.models.product.ProductModel;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponseModel {
    private int order_id;
    private List<OrderModel> orders;
    private List<ProductModel> products;
    @SerializedName("is_admin_confirm")
    private boolean is_admin_confirm;
    private double total_price, shipping_fee, total_product_price;

    public OrderResponseModel(int order_id, List<ProductModel> products, double shipping_fee, double total_price, double total_product_price) {
        this.order_id = order_id;
        this.products = products;
        this.shipping_fee = shipping_fee;
        this.total_price = total_price;
        this.total_product_price = total_product_price;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public List<ProductModel> getProductModels() {
        return products;
    }

    public void setProductModels(List<ProductModel> products) {
        this.products = products;
    }

    public double getShipping_fee() {
        return shipping_fee;
    }

    public void setShipping_fee(double shipping_fee) {
        this.shipping_fee = shipping_fee;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public double getTotal_product_price() {
        return total_product_price;
    }

    public void setTotal_product_price(double total_product_price) {
        this.total_product_price = total_product_price;
    }

    public boolean isIs_admin_confirm() {
        return is_admin_confirm;
    }

    public void setIs_admin_confirm(boolean is_admin_confirm) {
        this.is_admin_confirm = is_admin_confirm;
    }

    public List<OrderModel> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderModel> orders) {
        this.orders = orders;
    }
}