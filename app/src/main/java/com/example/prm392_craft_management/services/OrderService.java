package com.example.prm392_craft_management.services;

import com.example.prm392_craft_management.models.order.OrderRequestModel;
import com.example.prm392_craft_management.models.order.OrderResponseModel;
import com.example.prm392_craft_management.models.payment.PaymentRequestModel;
import com.example.prm392_craft_management.models.payment.PaymentResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface OrderService {
    String ORDER = "order";

    @POST(ORDER)
    Call<OrderResponseModel> createOrder(@Body OrderRequestModel orderRequestModel);

    @POST(ORDER + "/confirm/{cart_id}")
    Call<PaymentResponseModel> confirmOrder(@Path("cart_id") int cartId, @Body PaymentRequestModel orderRequestModel);
}
