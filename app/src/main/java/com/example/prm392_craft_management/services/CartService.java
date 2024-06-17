package com.example.prm392_craft_management.services;

import com.example.prm392_craft_management.models.cart.CartModel;
import com.example.prm392_craft_management.models.cart.CartResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CartService {
    String CART = "cart";

    @GET(CART + "/{id}")
    Call<CartResponseModel> getUserCart(
            @Path("id") String id,
            @Query("order") String sort
    );

    @POST(CART)
    Call<CartResponseModel> postUserCart(@Body CartModel cart);
}
