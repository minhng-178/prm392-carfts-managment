package com.example.prm392_craft_management.ui.cart;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.cart.CartModel;
import com.example.prm392_craft_management.models.cart.CartResponseModel;
import com.example.prm392_craft_management.repositories.CartRepository;
import com.example.prm392_craft_management.services.CartService;

import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartActivity extends AppCompatActivity {
    ImageButton buttonBack;
    ImageView ivEmptyCart;
    RecyclerView recyclerView;
    TextView tvTotalPrice;
    TextView tvTotalItems;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        buttonBack = findViewById(R.id.button_back);
        ivEmptyCart = findViewById(R.id.ivEmptyCart);
        recyclerView = findViewById(R.id.rvCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalItems = findViewById(R.id.tvTotalItems);


        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        updateCartUI();

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartUI();
    }

    private void updateCartUI() {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", "");

        CartService cartService = CartRepository.getCartService();
        cartService.getUserCart(userId, "asc").enqueue(new Callback<CartResponseModel>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<CartResponseModel> call, @NonNull Response<CartResponseModel> response) {
                if (response.isSuccessful()) {
                    List<CartModel> carts = response.body().getCart();
                    int totalItems = response.body().getTotal_items();
                    double totalPrice = 0;

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("TOTAL_ITEMS", totalItems);
                    editor.apply();

                    RecyclerView recyclerView = findViewById(R.id.rvCartItems);
                    ImageView ivEmptyCart = findViewById(R.id.ivEmptyCart);
                    TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
                    TextView tvTotalItems = findViewById(R.id.tvTotalItems);

                    if (carts.isEmpty()) {
                        ivEmptyCart.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        ivEmptyCart.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        CartAdapter adapter = new CartAdapter(carts);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                        recyclerView.setAdapter(adapter);
                        tvTotalItems.setText("Total Items: " + totalItems);
                        for (CartModel cart : carts) {
                            totalPrice += cart.getAmount() * cart.getPrice();
                        }
                        tvTotalPrice.setText(String.format(Locale.getDefault(), "Total Price: $%.2f", totalPrice));
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponseModel> call, @NonNull Throwable throwable) {
                Toast.makeText(CartActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
