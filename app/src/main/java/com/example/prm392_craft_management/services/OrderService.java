package com.example.prm392_craft_management.services;


import com.example.prm392_craft_management.models.order.OrderProductsResponseModel;
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
    String ORDER = "order";

    @GET(ORDER)
    Call<OrderResponseModel> getOrdersByUserId(@Query("user_id") int userId, @Query("order_by") String orderBy, @Query("status") int status, @Query("is_admin_confirm") boolean isAdminConfirm);

    @GET(ORDER + "/{order_id}")
    Call<OrderProductsResponseModel> getOrderById(@Path("order_id") int orderId);

    @POST(ORDER)
    Call<OrderResponseModel> createOrder(@Body OrderRequestModel orderRequestModel);

    @POST(ORDER + "/confirm/{cart_id}")
    Call<PaymentResponseModel> confirmOrder(@Path("cart_id") int cartId, @Body PaymentRequestModel orderRequestModel);
}

