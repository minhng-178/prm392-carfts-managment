package com.example.prm392_craft_management.ui.cart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
import com.example.prm392_craft_management.ui.checkout.CheckoutActivity;

import java.util.ArrayList;
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
    Button buttonCheckout, buttonCancel;
    CartAdapter cartAdapter;
    private boolean isSelectAllProgrammaticUpdate = false;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setTitle("Cart");
        cartAdapter = new CartAdapter(new ArrayList<>());
        buttonBack = findViewById(R.id.button_back);
        ivEmptyCart = findViewById(R.id.ivEmptyCart);
        recyclerView = findViewById(R.id.rvCartItems);
        tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalItems = findViewById(R.id.tvTotalItems);
        buttonCheckout = findViewById(R.id.button_checkout);
        buttonCancel = findViewById(R.id.button_cancel);

        buttonCancel.setEnabled(false);
        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());

        buttonCheckout.setOnClickListener(view -> {
            Intent intent = new Intent(CartActivity.this, CheckoutActivity.class);
            startActivity(intent);
        });

        CheckBox selectAllCheckbox = findViewById(R.id.checkbox_select_all);
        selectAllCheckbox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isSelectAllProgrammaticUpdate) {
                for (int i = 0; i < cartAdapter.getItemCount(); i++) {
                    cartAdapter.setItemSelected(i, isChecked);
                }
                cartAdapter.notifyDataSetChanged();
            }
        });

        buttonCancel.setOnClickListener(view -> {
            for (int i = 0; i < cartAdapter.getItemCount(); i++) {
                cartAdapter.setItemSelected(i, false);
            }
            cartAdapter.notifyDataSetChanged();

            updateCancelButtonState();
        });

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
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("TOTAL_ITEMS", totalItems);
                    editor.apply();

                    RecyclerView recyclerView = findViewById(R.id.rvCartItems);
                    ImageView ivEmptyCart = findViewById(R.id.ivEmptyCart);
                    TextView tvTotalItems = findViewById(R.id.tvTotalItems);

                    if (carts.isEmpty()) {
                        ivEmptyCart.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        ivEmptyCart.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        cartAdapter = new CartAdapter(carts);
                        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));
                        recyclerView.setAdapter(cartAdapter);
                        tvTotalItems.setText("Total Items: " + totalItems);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponseModel> call, @NonNull Throwable throwable) {
                Toast.makeText(CartActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void checkIfAllSelected() {
        isSelectAllProgrammaticUpdate = true;
        CheckBox selectAllCheckbox = findViewById(R.id.checkbox_select_all);
        selectAllCheckbox.setChecked(cartAdapter.areAllItemsSelected());
        isSelectAllProgrammaticUpdate = false;
    }

    public void updateCancelButtonState() {
        buttonCancel.setEnabled(cartAdapter.areAnyItemsSelected());
    }

    public void onCheckboxStateChanged() {
        updateTotalPrice();
        updateTotalItems();
    }

    private double calculateTotalPrice() {
        double totalPrice = 0.0;

        if (cartAdapter != null) {
            List<CartModel> cartList = cartAdapter.getCartList();
            for (int i = 0; i < cartList.size(); i++) {
                if (cartAdapter.isItemSelected(i)) {
                    CartModel cart = cartList.get(i);
                    totalPrice += cart.getAmount() * cart.getPrice();
                }
            }
        }

        return totalPrice;
    }

    private int calculateSelectedItemCount() {
        int count = 0;
        if (cartAdapter != null) {
            for (int i = 0; i < cartAdapter.getItemCount(); i++) {
                if (cartAdapter.isItemSelected(i)) {
                    count++;
                }
            }
        }
        return count;
    }

    private void updateTotalPrice() {
        double totalPrice = calculateTotalPrice();

        TextView tvTotalPrice = findViewById(R.id.tvTotalPrice);
        tvTotalPrice.setText(String.format(Locale.getDefault(), "Total Price: $%.2f", totalPrice));
    }

    @SuppressLint("SetTextI18n")
    private void updateTotalItems() {
        int selectedItemCount = calculateSelectedItemCount();

        Button buttonCheckout = findViewById(R.id.button_checkout);
        buttonCheckout.setText("Buy Now(" + selectedItemCount + ")");
    }
}
