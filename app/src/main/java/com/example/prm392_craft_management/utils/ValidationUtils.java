package com.example.prm392_craft_management.utils;

public class ValidationUtils {
    public static final int MAX_LENGTH_USERNAME = 20;
    public static final int MIN_LENGTH_PASSWORD = 8;
    public static final int PHONE_LENGTH = 10;
    public static final int MAX_ADDRESS_LENGTH = 50;

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidUsername(String userName) {
        return userName.length() < MAX_LENGTH_USERNAME;
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= MIN_LENGTH_PASSWORD;
    }

    public static boolean isValidPhone(String phone) {
        return phone.length() == PHONE_LENGTH;
    }

    public static boolean isValidAddress(String address) {
        return address.length() <= MAX_ADDRESS_LENGTH;
    }
}
