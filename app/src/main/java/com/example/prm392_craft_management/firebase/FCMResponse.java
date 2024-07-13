package com.example.prm392_craft_management.firebase;

public class FCMResponse {
    private  String name;

    public FCMResponse(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
