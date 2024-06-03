package com.example.prm392_craft_management.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.ui.register.RegisterActivity;

public class LoginActivity extends AppCompatActivity {
    Intent intent;
    TextView tvSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        tvSignUp = findViewById(R.id.tvSignUp);
        tvSignUp.setOnClickListener(v -> {
            intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });

    }
}
