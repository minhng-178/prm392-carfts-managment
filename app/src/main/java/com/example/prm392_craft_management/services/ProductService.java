package com.example.prm392_craft_management.services;

import com.example.prm392_craft_management.models.product.ProductModel;
import com.example.prm392_craft_management.models.product.ProductResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ProductService {
    String PRODUCT = "product";

    @GET(PRODUCT)
    Call<ProductResponseModel> getAllProducts(
            @Query("status") int status,
            @Query("order_direction") String orderDirection
    );

    @GET(PRODUCT + "/{id}")
    Call<ProductModel> getProductById(@Path("id") int id);
}
