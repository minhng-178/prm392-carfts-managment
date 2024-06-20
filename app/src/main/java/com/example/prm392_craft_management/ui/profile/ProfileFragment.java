package com.example.prm392_craft_management.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.prm392_craft_management.MainActivity;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.databinding.FragmentProfileBinding;
import com.example.prm392_craft_management.ui.login.LoginActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.util.Objects;


public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    GoogleSignInClient mGoogleSignInClient;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("USERNAME", "");

        String role = sharedPreferences.getString("ROLE", "");
        String token = sharedPreferences.getString("TOKEN", "");
        String photoUrl = sharedPreferences.getString("PHOTO_URL", "");

        TextView usernameTextView = binding.username;
        TextView roleTextView = binding.role;
        ImageView profileImageView = binding.avatar;
        Button loginButton = binding.loginBtn;
        Button logoutButton = binding.logoutBtn;

        if (username.isEmpty() || role.isEmpty() || token.isEmpty()) {
            usernameTextView.setVisibility(View.GONE);
            roleTextView.setVisibility(View.GONE);
            profileImageView.setVisibility(View.GONE);
            logoutButton.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        } else {
            profileImageView.setVisibility(View.VISIBLE);
            usernameTextView.setText(username);
            roleTextView.setText(role);
            loginButton.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);

            Glide.with(this)
                    .load(photoUrl)
                    .placeholder(R.drawable.avatar)
                    .into(profileImageView);
        }


        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        });

        logoutButton.setOnClickListener(v -> {
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();
            mGoogleSignInClient = GoogleSignIn.getClient(v.getContext(), gso);
            mGoogleSignInClient.signOut().addOnSuccessListener(getActivity(), task -> {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();
                Toast.makeText(getActivity(), "Logout Successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            });
        });

        return root;
    }

}
