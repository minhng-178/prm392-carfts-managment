package com.example.prm392_craft_management.models.image;

import java.io.Serializable;

public class ImageModel implements Serializable {
    private int id;
    private String url;
    private String image_url;

    public ImageModel(int id, String url, String image_url) {
        this.id = id;
        this.url = url;
        this.image_url = image_url;
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

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
