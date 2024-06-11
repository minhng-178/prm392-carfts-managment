package com.example.prm392_craft_management.models.festival;

import java.io.Serializable;
import java.util.Date;

public class FestivalModel implements Serializable {
    private int id, status;
    private String name, image, description, start_date, end_time;
    private boolean repeat_yearly;

    public FestivalModel(int id, int status, String name, String image, String description, String start_date, String end_time, boolean repeat_yearly) {
        this.id = id;
        this.status = status;
        this.name = name;
        this.image = image;
        this.description = description;
        this.start_date = start_date;
        this.end_time = end_time;
        this.repeat_yearly = repeat_yearly;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public boolean isRepeat_yearly() {
        return repeat_yearly;
    }

    public void setRepeat_yearly(boolean repeat_yearly) {
        this.repeat_yearly = repeat_yearly;
    }
}
