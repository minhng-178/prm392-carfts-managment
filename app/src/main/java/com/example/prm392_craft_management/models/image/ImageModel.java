package com.example.prm392_craft_management.models.image;

import java.io.Serializable;

public class ImageModel implements Serializable {
    private int id;
    private String url;

    public ImageModel(int id, String url) {
        this.id = id;
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
