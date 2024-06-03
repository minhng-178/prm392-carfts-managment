package com.example.prm392_craft_management.api;

import retrofit2.Retrofit;

public class APIClient {
    private static final String API_BASE_URL = "http://34.126.177.133:8881/v1/api";
    private static Retrofit retrofit;

    public static Retrofit getAPIClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .build();
        }
        return retrofit;
    }
}
