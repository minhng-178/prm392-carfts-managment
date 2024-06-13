package com.example.prm392_craft_management.ui.details;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.image.ImageModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.example.prm392_craft_management.models.product.ProductResponseModel;
import com.example.prm392_craft_management.repositories.ProductRepository;
import com.example.prm392_craft_management.services.ProductService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailActivity extends AppCompatActivity {
    ImageButton buttonBack;
    private TextView textName, textFestival, rate;
    private ImageView imageProduct, imageFestival;
    private RatingBar rating;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        buttonBack = findViewById(R.id.button_back);
        textName = findViewById(R.id.text_name);
        textFestival = findViewById(R.id.item_recommended_festival);
        rate = findViewById(R.id.rate);
        imageProduct = findViewById(R.id.image_product);
        imageFestival = findViewById(R.id.image_festival);
        rating = findViewById(R.id.rating);

        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());


        int productId = getIntent().getIntExtra("PRODUCT_ID", -1);
        if (productId != -1) {

            ProductService productService = ProductRepository.getProductService();
            productService.getProductById(productId).enqueue(new Callback<ProductModel>() {
                @Override
                public void onResponse(@NonNull Call<ProductModel> call, @NonNull Response<ProductModel> response) {
                    if (response.isSuccessful()) {
                        ProductModel product = response.body();
                        updateUI(product);
                    } else {
                        Log.d("API Response", "Response Code: " + response.code());
                        Log.d("API Message", "Response Message: " + response.message());
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
        textName.setText(product.getName());


        List<String> festivalNames = new ArrayList<>();
        for (FestivalModel festival : product.getFestivals()) {
            festivalNames.add(festival.getName());
        }
        String festivalsText = TextUtils.join(", ", festivalNames);
        textFestival.setText(festivalsText);

        // Load the product image
        if (!product.getImages().isEmpty()) {
            ImageModel imageModel = product.getImages().get(0);
            String imageUrl = imageModel.getUrl();
            Glide.with(imageProduct.getContext())
                    .load(imageUrl)
                    .into(imageProduct);
        }
    }
}

