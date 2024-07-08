package com.example.prm392_craft_management.models.payment;

public class PaymentRequestModel {
    private String type;

    public PaymentRequestModel(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
