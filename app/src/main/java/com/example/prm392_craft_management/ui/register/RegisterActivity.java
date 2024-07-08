package com.example.prm392_craft_management.ui.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.account.AccountModel;
import com.example.prm392_craft_management.models.account.AccountRequestModel;
import com.example.prm392_craft_management.models.account.AccountResponseModel;
import com.example.prm392_craft_management.repositories.AccountRepository;
import com.example.prm392_craft_management.services.AccountService;
import com.example.prm392_craft_management.ui.login.LoginActivity;
import com.example.prm392_craft_management.utils.ValidationUtils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import java.io.IOException;

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
            etUsername.setError("Username is required");
        } else if (!ValidationUtils.isValidUsername(username)) {
            etUsername.setError("Invalid username");
        }

        if (password.isEmpty()) {
            etPassword.setError("Password is required");
        } else if (!retypePassword.equals(password)) {
            etRetypePassword.setError("Password does not match");
        } else if (!ValidationUtils.isValidPassword(password)) {
            etPassword.setError("Password must contain at least 8 characters");
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
                            Toast.makeText(RegisterActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
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

            // Signed in successfully, show authenticated UI or proceed to next activity.
            // TODO: Implement your own logic here.
            Log.d("TAG", "handleSignInResult: " + account.getIdToken());

        } catch (ApiException e) {
            Log.w("TAG", "signInResult:failed code=" + e.getStatusCode());
            // TODO: Handle sign-in failure accordingly
        }
    }
}
