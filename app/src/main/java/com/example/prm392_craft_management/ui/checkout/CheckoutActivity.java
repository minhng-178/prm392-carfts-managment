package com.example.prm392_craft_management.ui.checkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.order.OrderRequestModel;
import com.example.prm392_craft_management.models.order.OrderResponseModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.example.prm392_craft_management.repositories.OrderRepository;
import com.example.prm392_craft_management.services.OrderService;
import com.example.prm392_craft_management.ui.review.ReviewActivity;
import com.example.prm392_craft_management.utils.NotificationUtils;
import com.example.prm392_craft_management.utils.ValidationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int FINE_PERMISSION_CODE = 1;
    private final LatLng SPECIFIC_ORIGIN = new LatLng(10.84127511221552, 106.80980789388458);
    private GoogleMap mMap;

    ImageView buttonBack;
    OrderRequestModel orderRequestModel;
    OrderService orderService;
    EditText etPhone, etAddress;
    Button btnCheckout;
    ArrayList<Integer> selectedCartIds;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    GeoApiContext geoApiContext;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.map_api_key))
                .build();

        orderService = OrderRepository.getOrderService();
        initComponents();
        initListeners();
    }

    private void initComponents() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
        etPhone = findViewById(R.id.etPhone);
        etAddress = findViewById(R.id.etAddress);
        btnCheckout = findViewById(R.id.btnCheckout);
        buttonBack = findViewById(R.id.button_back);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void initListeners() {
        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        btnCheckout.setOnClickListener(v -> handleCreateOrder());
    }

    private void handleCreateOrder() {
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();
        int distance = 100;
        String note = "String";
        if (phone.isEmpty()) {
            etPhone.setError("Phone is required");
            return;
        }
        if (!ValidationUtils.isValidPhone(phone)) {
            etPhone.setError("Invalid phone number (must be 10 digits)");
            return;
        }
        if (address.isEmpty()) {
            etAddress.setError("Address is required");
            return;
        }
        if (!ValidationUtils.isValidAddress(address)) {
            etAddress.setError("Address exceeds maximum length (50 characters)");
            return;
        }
        Intent intent = getIntent();
        if (intent != null) {
            selectedCartIds = intent.getIntegerArrayListExtra("SELECTED_CART_IDS");
        }

        SharedPreferences sharedPreferences = getSharedPreferences("User_Info", MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", "");
        if (selectedCartIds != null && !selectedCartIds.isEmpty() && !userId.isEmpty()) {
            orderRequestModel = new OrderRequestModel(address, selectedCartIds, distance, note, phone, Integer.parseInt(userId));
        }
        btnCheckout.setEnabled(false);
        btnCheckout.setClickable(false);

        orderService.createOrder(orderRequestModel).enqueue(new Callback<OrderResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<OrderResponseModel> call, @NonNull Response<OrderResponseModel> response) {
                if (response.isSuccessful()) {
                    OrderResponseModel orderResponseModel = response.body();
                    Intent intent = new Intent(CheckoutActivity.this, ReviewActivity.class);
                    Gson gson = new Gson();
                    String orderResponseJson = gson.toJson(orderResponseModel);
                    intent.putExtra("ORDER_RESPONSE_JSON", orderResponseJson);
                    startActivity(intent);
                    showNotification();
                    finish();
                } else {
                    Toast.makeText(CheckoutActivity.this, "Failed to create new order", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<OrderResponseModel> call, @NonNull Throwable throwable) {
                Toast.makeText(CheckoutActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
        btnCheckout.setEnabled(true);
        btnCheckout.setClickable(true);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (currentLocation != null) {

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                return;
            }

            LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLocation).title("My Location");
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
//            mMap.addMarker(markerOptions);
            mMap.setMyLocationEnabled(true);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setZoomGesturesEnabled(true);
            mMap.getUiSettings().setScrollGesturesEnabled(true);

        }
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    currentLocation = location;
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    calculateDistance(latLng, SPECIFIC_ORIGIN);
                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(CheckoutActivity.this);
                }
            }
        });
    }

    private void calculateDistance(LatLng origin, LatLng destination) {
        DirectionsApiRequest req = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude));
        try {
            DirectionsResult res = req.await();
            long distanceInMeters = 0;
            for (DirectionsLeg leg : res.routes[0].legs) {
                distanceInMeters += leg.distance.inMeters;
            }
            Log.i("TAG", "The distance is: " + distanceInMeters + " meters.");
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == FINE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showNotification() {
        NotificationUtils notificationUtils = new NotificationUtils(this, "CHANNEL_ID");
        notificationUtils.showNotification("Order Successful!", "One more step to complete your order");
    }
}