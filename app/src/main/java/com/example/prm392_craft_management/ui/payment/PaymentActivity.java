package com.example.prm392_craft_management.ui.payment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

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
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("TAG", url);
                if (isPaymentSuccessful(url)) {
                    showPaymentSuccessFragment();
                    webView.setVisibility(View.INVISIBLE);
                }
            }
        });

        if (paymentUrl != null) {
            webView.loadUrl(paymentUrl);
        }
    }

    private boolean isPaymentSuccessful(String url) {
        return url.contains("vnp_ResponseCode=00");
    }

    private void showPaymentSuccessFragment() {
        PaymentSuccessFragment fragment = new PaymentSuccessFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }
}

