package com.example.prm392_craft_management.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.ui.login.LoginActivity;

public class RegisterActivity extends AppCompatActivity {

    Intent intent;
    TextView tvSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        tvSignIn = findViewById(R.id.tvSignIn);

        tvSignIn.setOnClickListener(v -> {
            intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }
}
