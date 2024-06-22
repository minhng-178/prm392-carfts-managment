package com.example.prm392_craft_management.models.order;

import com.example.prm392_craft_management.models.product.ProductModel;

import java.util.List;

public class OrderResponseModel {
    private int order_id;
    private List<ProductModel> products;
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
}
