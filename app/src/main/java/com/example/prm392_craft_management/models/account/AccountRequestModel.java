package com.example.prm392_craft_management.models.account;

public class AccountRequestModel {
    String username, password, retype_password;

    public AccountRequestModel(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AccountRequestModel(String username, String password, String retype_password) {
        this.username = username;
        this.password = password;
        this.retype_password = retype_password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRetype_password() {
        return retype_password;
    }

    public void setRetype_password(String retype_password) {
        this.retype_password = retype_password;
    }
}
