package com.example.prm392_craft_management;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.prm392_craft_management.databinding.ActivityMainBinding;
import com.example.prm392_craft_management.ui.chat.MessageActivity;
import com.example.prm392_craft_management.ui.cart.CartActivity;
import com.example.prm392_craft_management.utils.NotificationUtils;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TextView cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1);
            }
        }

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_order, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        navView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.navigation_home) {
                    navController.navigate(R.id.navigation_home);
                    return true;
                } else if (id == R.id.navigation_search) {
                    navController.navigate(R.id.navigation_search);
                    return true;
                } else if (id == R.id.navigation_order) {
                    navController.navigate(R.id.navigation_order);
                    return true;
                } else if (id == R.id.navigation_profile) {
                    navController.navigate(R.id.navigation_profile);
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.action_cart);
        item.setActionView(R.layout.actionbar_cart_icon);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        RelativeLayout cartView = (RelativeLayout) cartItem.getActionView();
        assert cartView != null;
        cartBadge = cartView.findViewById(R.id.cart_badge);

        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        int totalItems = sharedPreferences.getInt("TOTAL_ITEMS", 0);

        cartBadge.setText(String.valueOf(totalItems));

        cartView.setOnClickListener(v -> {
            startActivity(new Intent(this, CartActivity.class));
        });

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String name = "Admin";
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", "");
        int itemId = item.getItemId();
        if (Integer.parseInt(userId) == -1) {
            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
        } else {
            if (itemId == R.id.action_message) {
                Intent chatIntent = new Intent(this, MessageActivity.class);
                startActivity(chatIntent);
                chatIntent.putExtra("name", name);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onStart() {
        super.onStart();
        updateCartCount();
        showCartNotificationIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    @Override
    protected void onStop() {
        super.onStop();
        cartBadge.setText("0");
    }

    public void updateCartCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        int totalItems = sharedPreferences.getInt("TOTAL_ITEMS", 0);
        if (cartBadge != null) {
            cartBadge.setText(String.valueOf(totalItems));
        }
    }

    private void showCartNotificationIfNeeded() {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        int totalItems = sharedPreferences.getInt("TOTAL_ITEMS", 0);

        if (totalItems > 0) {
            showNotification();
        }
    }

    private void showNotification() {
        NotificationUtils notificationUtils = new NotificationUtils(this, "CHANNEL_ID");
        notificationUtils.showNotification("You have items in your cart!", "Don't forget to complete your purchase.");
    }
}