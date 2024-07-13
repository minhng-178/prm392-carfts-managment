package com.example.prm392_craft_management.ui.register;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_craft_management.MainActivity;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.account.AccountModel;
import com.example.prm392_craft_management.models.account.AccountRequestModel;
import com.example.prm392_craft_management.models.account.AccountResponseModel;
import com.example.prm392_craft_management.repositories.AccountRepository;
import com.example.prm392_craft_management.services.AccountService;
import com.example.prm392_craft_management.ui.login.LoginActivity;
import com.example.prm392_craft_management.utils.NotificationUtils;
import com.example.prm392_craft_management.utils.ValidationUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    Intent intent;
    TextView tvSignIn, etUsername, etPassword, etRetypePassword;
    Button signUpButton;
    AccountService accountService;
    SignInButton signInGoogle;
    GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 1000;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        accountService = AccountRepository.getAccountService();
        initComponents();
        initListeners();
    }

    private void initComponents() {
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etRetypePassword = findViewById(R.id.etRetypePassword);
        signUpButton = findViewById(R.id.signUpButton);
        tvSignIn = findViewById(R.id.tvSignIn);
    }

    private void initListeners() {
        signUpButton.setOnClickListener(v -> {
            if (!validateInputs()) {
                return;
            }
            doSignUp();
        });

//        signInGoogle.setOnClickListener(view -> {
//            Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//            startActivityForResult(signInIntent, RC_SIGN_IN);
//        });

        tvSignIn.setOnClickListener(v -> {
            intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });
    }

    private boolean validateInputs() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        String retypePassword = etRetypePassword.getText().toString();

        if (username.isEmpty()) {
            etUsername.setError("Yêu cầu tên tài khoản");
        } else if (!ValidationUtils.isValidUsername(username)) {
            etUsername.setError("Sai tên tài khoản");
        }

        if (password.isEmpty()) {
            etPassword.setError("Yêu cầu mật khẩu");
        } else if (!retypePassword.equals(password)) {
            etRetypePassword.setError("Sai mật khẩu");
        } else if (!ValidationUtils.isValidPassword(password)) {
            etPassword.setError("Mật khẩu cần ít nhất 8 ký tự");
        }
        return !username.isEmpty()
                && ValidationUtils.isValidUsername(username)
                && !password.isEmpty()
                && ValidationUtils.isValidPassword(password)
                && retypePassword.equals(password);
    }

    private void doSignUp() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString();
        String retypePassword = etRetypePassword.getText().toString();

        AccountRequestModel accountSignUp = new AccountRequestModel(username, password, retypePassword);
        signUpButton.setClickable(false);
        signUpButton.setEnabled(false);
        accountService.register(accountSignUp)
                .enqueue(new Callback<AccountResponseModel>() {
                    @Override
                    public void onResponse(@NonNull Call<AccountResponseModel> call, @NonNull Response<AccountResponseModel> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(RegisterActivity.this, "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            try {
                                String errorBody = response.errorBody().string();

                                if (response.code() == 400) {
                                    errorBody = "Username already exists";
                                }

                                Toast.makeText(RegisterActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                            } catch (IOException e) {
                                Toast.makeText(RegisterActivity.this, "Something went wrong with our server. Please try again later.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AccountResponseModel> call, @NonNull Throwable throwable) {
                        Toast.makeText(RegisterActivity.this, "Something went wrong with our server. Please try again later.", Toast.LENGTH_SHORT).show();
                    }
                });
        signUpButton.setEnabled(true);
        signUpButton.setClickable(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            accountService.loginWithGoogle(account.getIdToken()).enqueue(new Callback<AccountResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<AccountResponseModel> call, @NonNull Response<AccountResponseModel> response) {
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        assert response.body() != null;
                        editor.putString("USERNAME", account.getDisplayName());
                        editor.putString("EMAIL", account.getEmail());
                        editor.putString("PHOTO_URL", Objects.requireNonNull(account.getPhotoUrl()).toString());
                        editor.putString("TOKEN", response.body().getResult().getToken());
                        editor.putString("ROLE", response.body().getResult().getUser().getRole().getName());
                        editor.putString("USER_ID", String.valueOf(response.body().getResult().getUser().getId()));
                        editor.apply();
                        showNotification();
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

                            Log.d("TAG", errorBody);

                            Toast.makeText(RegisterActivity.this, errorBody, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            Toast.makeText(RegisterActivity.this, "Something went wrong with our server. Please try again later.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<AccountResponseModel> call, @NonNull Throwable throwable) {

                }
            });
            Log.d("TAG", "IdToken: " + account.getIdToken());

        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
        }
    }

    private void showNotification() {
        NotificationUtils notificationUtils = new NotificationUtils(this, "CHANNEL_ID");
        notificationUtils.showNotification("Đăng ký thành công!", "Chào mừng bạn", false);
    }
}
