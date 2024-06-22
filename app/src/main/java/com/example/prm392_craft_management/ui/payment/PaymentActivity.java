package com.example.prm392_craft_management.ui.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_craft_management.R;

public class PaymentActivity extends AppCompatActivity {
    WebView webView;
    ImageView buttonBack;

    @SuppressLint({"MissingInflatedId", "SetJavaScriptEnabled"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = getIntent();
        String paymentUrl = intent.getStringExtra("PAYMENT_URL");
        webView = findViewById(R.id.wvPayment);
        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        if (paymentUrl != null) {
            webView.loadUrl(paymentUrl);
        }

    }
}
