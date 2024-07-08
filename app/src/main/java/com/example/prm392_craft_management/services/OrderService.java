package com.example.prm392_craft_management.services;

import com.example.prm392_craft_management.models.order.OrderModel;
import com.example.prm392_craft_management.models.order.OrderResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface OrderService {

    @GET("order")
    Call<OrderResponseModel> getOrdersByUserId(@Query("user_id") int userId);

}