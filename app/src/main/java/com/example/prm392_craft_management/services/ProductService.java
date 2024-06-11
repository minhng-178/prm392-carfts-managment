package com.example.prm392_craft_management.services;

import com.example.prm392_craft_management.models.product.ProductResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProductService {
    String PRODUCT = "product";

    @GET(PRODUCT)
    Call<ProductResponseModel> getAllProducts();

    @GET(PRODUCT + "/{id}")
    Call<ProductResponseModel> getProductById(@Path("id") int id);
}
