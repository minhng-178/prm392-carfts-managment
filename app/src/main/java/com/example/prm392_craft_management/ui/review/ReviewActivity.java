package com.example.prm392_craft_management.ui.review;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.MainActivity;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.order.OrderResponseModel;
import com.example.prm392_craft_management.models.payment.PaymentRequestModel;
import com.example.prm392_craft_management.models.payment.PaymentResponseModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.example.prm392_craft_management.repositories.OrderRepository;
import com.example.prm392_craft_management.services.OrderService;
import com.example.prm392_craft_management.ui.order.OrderFragment;
import com.example.prm392_craft_management.ui.payment.PaymentActivity;
import com.example.prm392_craft_management.utils.NotificationUtils;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReviewActivity extends AppCompatActivity {
    OrderService orderService;
    Button btnSubmit;
    ImageView buttonBack;
    OrderResponseModel orderResponseModel;
    TextView tvOrderId, tvTotalProductPrice, tvShippingFee, tvTotalPrice;
    Spinner spinnerPaymentMethod;
    String selectedPaymentMethod = "online"; // Default payment method

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        Gson gson = new Gson();
        String orderResponseJson = getIntent().getStringExtra("ORDER_RESPONSE_JSON");
        orderResponseModel = gson.fromJson(orderResponseJson, OrderResponseModel.class);
        List<ProductModel> productList = orderResponseModel.getProductModels();

        orderService = OrderRepository.getOrderService();
        if (orderResponseModel != null && productList != null) {
            initComponents();
            initListeners();
            RecyclerView recyclerView = findViewById(R.id.recycleview_confirm);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            ReviewAdapter reviewAdapter = new ReviewAdapter(productList);
            recyclerView.setAdapter(reviewAdapter);
        } else {
            Log.d("TAG", "OrderResponseModel is null");
        }

        btnSubmit.setOnClickListener(v -> {
            handlePayment(orderResponseModel.getOrder_id());
        });
    }

    private void initComponents() {
        btnSubmit = findViewById(R.id.btnSubmit);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTotalProductPrice = findViewById(R.id.tvTotalProductPrice);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        buttonBack = findViewById(R.id.button_back);
        spinnerPaymentMethod = findViewById(R.id.spinner_payment_method);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.payment_methods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPaymentMethod.setAdapter(adapter);
    }

    @SuppressLint("SetTextI18n")
    private void initListeners() {
        tvOrderId.setText("Mã đơn: #" + orderResponseModel.getOrder_id());
        tvTotalProductPrice.setText("Tổng tiền của đơn hàng: " + orderResponseModel.getTotal_product_price() + " VND");
        tvShippingFee.setText("Shipping Fee: " + orderResponseModel.getShipping_fee() + " VND");
        tvTotalPrice.setText("Total Price: " + orderResponseModel.getTotal_price() + " VND");
        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        spinnerPaymentMethod.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedPaymentMethod = parent.getItemAtPosition(position).toString().toLowerCase();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void handlePayment(int cartId) {
        PaymentRequestModel paymentRequestModel = new PaymentRequestModel(selectedPaymentMethod);


        orderService.confirmOrder(cartId, paymentRequestModel).enqueue(new Callback<PaymentResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<PaymentResponseModel> call, @NonNull Response<PaymentResponseModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ReviewActivity.this, "Thành công", Toast.LENGTH_SHORT).show();
                    showNotification();
                    if (selectedPaymentMethod.equals("online")) {
                        assert response.body() != null;
                        Intent intent = new Intent(ReviewActivity.this, PaymentActivity.class);
                        intent.putExtra("PAYMENT_URL", response.body().getPayment().getUrl());
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(ReviewActivity.this, MainActivity.class);
                        intent.putExtra("navigateToOrder", true);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(ReviewActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<PaymentResponseModel> call, @NonNull Throwable throwable) {
                Toast.makeText(ReviewActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNotification() {
        NotificationUtils notificationUtils = new NotificationUtils(this, "CHANNEL_ID");
        notificationUtils.showNotification("Xác nhận đơn hàng thành công!", "Đơn hàng của bạn đã được xác nhận", false);
    }
}
