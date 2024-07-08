package com.example.prm392_craft_management.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_craft_management.MainActivity;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.account.AccountModel;
import com.example.prm392_craft_management.models.account.AccountResponseModel;
import com.example.prm392_craft_management.repositories.AccountRepository;
import com.example.prm392_craft_management.services.AccountService;
import com.example.prm392_craft_management.ui.register.RegisterActivity;
import com.example.prm392_craft_management.utils.ValidationUtils;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Intent intent;
    TextView tvSignUp;
    Button signInButton;
    EditText etUsername, etPassword;
    AccountService accountService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "");
        String token = sharedPreferences.getString("TOKEN", "");
        String role = sharedPreferences.getString("ROLE", "");
        if (!username.isEmpty() && !token.isEmpty() && !role.isEmpty()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        accountService = AccountRepository.getAccountService();
        initComponents();
        initListeners();


    }

    private void initComponents() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        signInButton = findViewById(R.id.signInButton);
        tvSignUp = findViewById(R.id.tvSignUp);
    }

    private void initListeners() {
        signInButton.setOnClickListener(v -> {
            if (!validateInputs()) {
                signInButton.setEnabled(true);
                return;
            }
            doLogin();
        });

        tvSignUp.setOnClickListener(v -> {
            intent = new Intent(this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();

        if (username.isEmpty()) {
            etUsername.setError("Email is required");
        } else if (!ValidationUtils.isValidUsername(username)) {
            etUsername.setError("Invalid email");
        }
        if (password.isEmpty()) {
            etPassword.setError("Password is required");
        }
        return !username.isEmpty() && ValidationUtils.isValidUsername(username) && !password.isEmpty();
    }

    private void doLogin() {
        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();

        AccountModel loginAccount = new AccountModel(username, password);
        signInButton.setClickable(false);
        signInButton.setEnabled(false);

        accountService.login(loginAccount).enqueue(new Callback<AccountResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<AccountResponseModel> call, @NonNull Response<AccountResponseModel> response) {
                if (response.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    assert response.body() != null;
                    editor.putString("USERNAME", username);
                    editor.putString("TOKEN", response.body().getResult().getToken());
                    editor.putString("ROLE", response.body().getResult().getUser().getRole().getName());
                    editor.putInt("USER_ID", response.body().getResult().getUser().getId());
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                    finish();
                } else {
                    try {
                        assert response.errorBody() != null;
                        String errorBody = response.errorBody().string();


                        if (response.code() == 401) {
                            errorBody = "Unauthenticated";
                        } else if (response.code() == 400) {
                            errorBody = "Invalid username or password";
                        } else if (response.code() == 404) {
                            errorBody = "User not found";
                        }

                        Toast.makeText(LoginActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(LoginActivity.this, "Something went wrong with our server. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<AccountResponseModel> call, @NonNull Throwable throwable) {
                Toast.makeText(LoginActivity.this, "Something went wrong with our server. Please try again later.", Toast.LENGTH_SHORT).show();
            }
        });
        signInButton.setEnabled(true);
        signInButton.setClickable(true);
    }
}
