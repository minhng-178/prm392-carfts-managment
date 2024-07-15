package com.example.prm392_craft_management.ui.details;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.MainActivity;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.order.OrderProductsModel;
import com.example.prm392_craft_management.models.order.OrderProductsResponseModel;
import com.example.prm392_craft_management.models.order.OrderResponseModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.example.prm392_craft_management.repositories.OrderRepository;
import com.example.prm392_craft_management.services.OrderService;
import com.example.prm392_craft_management.ui.order.OrderDetailAdapter;
import com.example.prm392_craft_management.ui.review.ReviewAdapter;
import com.example.prm392_craft_management.utils.DateUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailActivity extends AppCompatActivity {
    OrderService orderService;
    Button btnSubmit;
    ImageView buttonBack;
    OrderProductsResponseModel orderProductsResponseModel;
    TextView tvOrderId, tvTotalProductPrice, tvShippingFee, tvTotalPrice, tvStatus, tvAddress, tvPhone, tvUsername, tvCreateAt, tvIsConfirmed;
    int orderId;
    String username;
    List<ProductModel> productList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        initComponents();
        initListeners();
    }

    private void initComponents() {
        btnSubmit = findViewById(R.id.btnSubmit);
        buttonBack = findViewById(R.id.button_back);
        tvOrderId = findViewById(R.id.tvOrderId);
        tvTotalProductPrice = findViewById(R.id.tvTotalProductPrice);
        tvShippingFee = findViewById(R.id.tvShippingFee);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvStatus = findViewById(R.id.tvStatus);
        tvCreateAt = findViewById(R.id.tvCreateAt);
        tvAddress = findViewById(R.id.tvAddress);
        tvPhone = findViewById(R.id.tvPhone);
        tvUsername = findViewById(R.id.tvUsername);
        tvIsConfirmed = findViewById(R.id.tvIsConfirmed);
        orderId = getIntent().getIntExtra("ORDER_ID", -1);
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("USERNAME", "");
    }


    private void initListeners() {
        fetchOrderDetails();
        btnSubmit.setOnClickListener(view -> {
            Intent intent = new Intent(OrderDetailActivity.this, MainActivity.class);
            startActivity(intent);
        });
        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
    }

    private void fetchOrderDetails() {
        if (orderId != -1) {
            orderService = OrderRepository.getOrderService();
            orderService.getOrderById(orderId).enqueue(new Callback<OrderProductsResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<OrderProductsResponseModel> call, @NonNull Response<OrderProductsResponseModel> response) {
                    if (response.isSuccessful()) {
                        orderProductsResponseModel = response.body();
                        updateUI();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OrderProductsResponseModel> call, @NonNull Throwable throwable) {
                    Toast.makeText(OrderDetailActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @SuppressLint("SetTextI18n")
    private void updateUI() {
        setTitle("Đơn hàng " + "#" + orderId);
        tvOrderId.setText("Mã đơn: #" + orderProductsResponseModel.getOrder().getId());
        tvTotalProductPrice.setText("Tổng tiền sản phẩm: " + orderProductsResponseModel.getOrder().getTotalProductPrice() + "VND");
        tvShippingFee.setText("Phí ship: " + orderProductsResponseModel.getOrder().getShippingFee() + "VND");
        tvTotalPrice.setText("Tổng tiền: " + orderProductsResponseModel.getOrder().getTotalPrice() + "VND");
        tvPhone.setText("Số điện thoại: " + orderProductsResponseModel.getOrder().getPhone());
        tvAddress.setText("Địa chỉ: " + orderProductsResponseModel.getOrder().getAddress());
        String createdAt = orderProductsResponseModel.getOrder().getCreatedAt();
        String formattedDate = DateUtils.formatDate(createdAt);
        tvCreateAt.setText("Ngày đặt: " + formattedDate);
        tvUsername.setText(username);
        tvStatus.setText(getStatusText(orderProductsResponseModel.getOrder().getStatus()));
        tvStatus.setTextColor(getStatusColor(orderProductsResponseModel.getOrder().getStatus()));
        if (orderProductsResponseModel.getOrder().isAdminConfirm()) {
            tvIsConfirmed.setText(getTextWithImage("Đã xác nhận: ", R.drawable.ic_confirm));
        } else {
            tvIsConfirmed.setText(getTextWithImage("Chưa xác nhận: ", R.drawable.ic_cancel));
        }

        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        productList = orderProductsResponseModel.getOrder().getProducts();
        RecyclerView recyclerView = findViewById(R.id.recycleview_orderproducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        OrderDetailAdapter orderDetailAdapter = new OrderDetailAdapter(productList);
        recyclerView.setAdapter(orderDetailAdapter);
    }

    private String getStatusText(int status) {
        switch (status) {
            case 0:
                return "Hủy";
            case 1:
                return "Processing";
            case 2:
                return "Confirm";
            case 3:
                return "Đã thanh toán (Online)";
            case 4:
                return "Ship Code";
            case 5:
                return "Hide";
            case 6:
                return "Refunding";
            case 7:
                return "Refunded";
            default:
                return "Unknown";
        }
    }

    private int getStatusColor(int status) {
        Context context = OrderDetailActivity.this;
        switch (status) {
            case 0:
                return ContextCompat.getColor(context, R.color.status_cancelled);
            case 1:
                return ContextCompat.getColor(context, R.color.status_processing);
            case 2:
                return ContextCompat.getColor(context, R.color.status_confirmed);
            case 3:
                return ContextCompat.getColor(context, R.color.status_paid);
            case 4:
                return ContextCompat.getColor(context, R.color.status_ship_code);
            case 5:
                return ContextCompat.getColor(context, R.color.status_hidden);
            case 6:
                return ContextCompat.getColor(context, R.color.status_refunding);
            case 7:
                return ContextCompat.getColor(context, R.color.status_refunded);
            default:
                return ContextCompat.getColor(context, R.color.status_unknown);
        }
    }

    private SpannableString getTextWithImage(String text, int drawableResId) {
        SpannableString spannableString = new SpannableString(text + " ");

        Drawable drawable = ContextCompat.getDrawable(this, drawableResId);
        if (drawable != null) {
            // Tint the drawable based on the drawable resource ID
            if (drawableResId == R.drawable.ic_confirm) {
                DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.status_paid));
            } else if (drawableResId == R.drawable.ic_cancel) {
                DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.red_orange));
            }

            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
            spannableString.setSpan(imageSpan, text.length(), text.length() + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        return spannableString;
    }
}
