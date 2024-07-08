package com.example.prm392_craft_management.services;


import com.example.prm392_craft_management.models.order.OrderRequestModel;
import com.example.prm392_craft_management.models.order.OrderResponseModel;
import com.example.prm392_craft_management.models.payment.PaymentRequestModel;
import com.example.prm392_craft_management.models.payment.PaymentResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderService {

    @GET("order")
    Call<OrderResponseModel> getOrdersByUserId(@Query("user_id") int userId);
    String ORDER = "order";

    @POST(ORDER)
    Call<OrderResponseModel> createOrder(@Body OrderRequestModel orderRequestModel);

    @POST(ORDER + "/confirm/{cart_id}")
    Call<PaymentResponseModel> confirmOrder(@Path("cart_id") int cartId, @Body PaymentRequestModel orderRequestModel);
}

