package com.example.prm392_craft_management;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.prm392_craft_management.databinding.ActivityMainBinding;
import com.example.prm392_craft_management.ui.cart.CartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private TextView cartBadge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_search, R.id.navigation_order, R.id.navigation_profile)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
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
    protected void onResume() {
        super.onResume();
        updateCartCount();
    }

    public void updateCartCount() {
        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        int totalItems = sharedPreferences.getInt("TOTAL_ITEMS", 0);
        if (cartBadge != null) {
            cartBadge.setText(String.valueOf(totalItems));
        }
    }
}