package com.example.prm392_craft_management.ui.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.cart.CartModel;
import com.example.prm392_craft_management.models.cart.CartResponseModel;
import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.image.ImageModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.example.prm392_craft_management.repositories.CartRepository;
import com.example.prm392_craft_management.repositories.ProductRepository;
import com.example.prm392_craft_management.services.CartService;
import com.example.prm392_craft_management.services.ProductService;
import com.example.prm392_craft_management.ui.cart.CartActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    ImageButton buttonBack;
    Button addToCart, btnDecreaseQuantity, btnIncreaseQuantity;
    TextView textName, textFestival, textPrice, textWeight, textDescription, textAmount, rate, tvQuantity;
    ImageView imageProduct;
    RatingBar rating;
    private int productId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        initComponents();
        initListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchProductDetailsAndUpdateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchProductDetailsAndUpdateUI();
    }

    private void initComponents() {
        addToCart = findViewById(R.id.addToCart);
        buttonBack = findViewById(R.id.button_back);
        textName = findViewById(R.id.text_name);
        textFestival = findViewById(R.id.item_search_festival);
        rate = findViewById(R.id.rate);
        textPrice = findViewById(R.id.text_price);
        textWeight = findViewById(R.id.text_weight);
        textDescription = findViewById(R.id.text_description);
        imageProduct = findViewById(R.id.image_product);
        textAmount = findViewById(R.id.text_amount);
        rating = findViewById(R.id.rating);
        btnDecreaseQuantity = findViewById(R.id.btnDecreaseQuantity);
        btnIncreaseQuantity = findViewById(R.id.btnIncreaseQuantity);
        tvQuantity = findViewById(R.id.tvQuantity);
        productId = getIntent().getIntExtra("PRODUCT_ID", -1);
    }

    private void initListeners() {
        fetchProductDetailsAndUpdateUI();
        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        addToCart.setOnClickListener(view -> addToCart());
    }

    private void fetchProductDetailsAndUpdateUI() {
        if (productId != -1) {
            ProductService productService = ProductRepository.getProductService();
            productService.getProductById(productId).enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(@NonNull Call<ProductModel> call, @NonNull Response<ProductModel> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateUI(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ProductModel> call, @NonNull Throwable t) {
                    Log.d("API Error", Objects.requireNonNull(t.getMessage()));
                }
            });
        }
    }

    private void updateUI(ProductModel product) {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        int storedQuantity = sharedPreferences.getInt("PRODUCT_" + productId, 1);
        int[] quantity = {storedQuantity};
        btnDecreaseQuantity.setOnClickListener(view -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                tvQuantity.setText(String.valueOf(quantity[0]));
            } else {
                Toast.makeText(this, "Số lượng sản phẩm phải lớn hơn 1", Toast.LENGTH_SHORT).show();
            }
        });

        btnIncreaseQuantity.setOnClickListener(view -> {
            quantity[0]++;
            tvQuantity.setText(String.valueOf(quantity[0]));
        });

        textName.setText(product.getName());

        List<String> festivalNames = new ArrayList<>();
        for (FestivalModel festival : product.getFestivals()) {
            festivalNames.add(festival.getName());
        }
        String festivalsText = TextUtils.join(", ", festivalNames);
        textFestival.setText(festivalsText);

        if (!product.getImages().isEmpty()) {
            ImageModel imageModel = product.getImages().get(0);
            String imageUrl = imageModel.getUrl();
            Glide.with(imageProduct.getContext()).load(imageUrl).into(imageProduct);
        }

        textPrice.setText(String.format(Locale.getDefault(), "%.2f", product.getPrice(), "VND"));
        textWeight.setText(String.format(Locale.getDefault(), "Cân nặng: %.2f", product.getWeight()));
        textAmount.setText(String.format(Locale.getDefault(), "Số lượng: %d", product.getAmount()));
        textDescription.setText(product.getDescription());
        tvQuantity.setText(String.valueOf(storedQuantity));
    }

    private void addToCart() {
        int currentQuantity = Integer.parseInt(tvQuantity.getText().toString());

        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", "");

        if (userId.isEmpty() || !userId.matches("\\d+")) {
            Toast.makeText(ProductDetailActivity.this, "Xin hãy đăng nhập trước", Toast.LENGTH_SHORT).show();
            return;
        }

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("PRODUCT_" + productId, currentQuantity);
        editor.apply();

        List<CartModel.CartProduct> cartProducts = new ArrayList<>();
        cartProducts.add(new CartModel.CartProduct(productId, currentQuantity));
        CartModel cart = new CartModel(Integer.parseInt(userId), cartProducts);
        CartService cartService = CartRepository.getCartService();
        cartService.postUserCart(cart).enqueue(new Callback<CartResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<CartResponseModel> call, @NonNull Response<CartResponseModel> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ProductDetailActivity.this, CartActivity.class));
                } else if (response.code() == 400) {
                    Toast.makeText(ProductDetailActivity.this, "Số lượng đơn hàng hiện tại đã vượt quá giới hạn", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CartResponseModel> call, @NonNull Throwable throwable) {
                Toast.makeText(ProductDetailActivity.this, "Lỗi khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

