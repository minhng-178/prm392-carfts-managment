package com.example.prm392_craft_management.ui.checkout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.order.OrderRequestModel;
import com.example.prm392_craft_management.models.order.OrderResponseModel;
import com.example.prm392_craft_management.repositories.OrderRepository;
import com.example.prm392_craft_management.services.OrderService;
import com.example.prm392_craft_management.ui.map.PlaceAutocompleteDialogFragment;
import com.example.prm392_craft_management.ui.review.ReviewActivity;
import com.example.prm392_craft_management.utils.NotificationUtils;
import com.example.prm392_craft_management.utils.ValidationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final int FINE_PERMISSION_CODE = 1;
    private final LatLng SPECIFIC_ORIGIN = new LatLng(10.84127511221552, 106.80980789388458);
    private boolean isLocationUpdated = false;
    private GoogleMap mMap;
    private double distanceInKilometers = 0.0;

    Button btnSetLocation;
    ImageView buttonBack;
    OrderRequestModel orderRequestModel;
    OrderService orderService;
    EditText etPhone;
    TextView etAddress, tvDistance;
    Button btnCheckout;
    ArrayList<Integer> selectedCartIds;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    AutocompleteSupportFragment autocompleteFragment;
    SupportMapFragment mapFragment;
    GeoApiContext geoApiContext;
    LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        geoApiContext = new GeoApiContext.Builder()
                .apiKey(getString(R.string.map_api_key))
                .build();
        Places.initialize(getApplicationContext(), getString(R.string.map_api_key));
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
        tvDistance = findViewById(R.id.tvDistance);
        btnSetLocation = findViewById(R.id.btnSetLocation);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment);
    }

    private void initListeners() {
        buttonBack.setOnClickListener(view -> getOnBackPressedDispatcher().onBackPressed());
        btnCheckout.setOnClickListener(v -> handleCreateOrder());
        btnSetLocation.setOnClickListener(v -> setCurrentLocationAsAddress());
        etAddress.setOnClickListener(v -> showPlaceAutocompleteDialog());
    }

    private void handleCreateOrder() {
        String phone = etPhone.getText().toString();
        String address = etAddress.getText().toString();
        int distance = (int) (distanceInKilometers);
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
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        if (currentLocation != null) {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                return;
            }

            LatLng myLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            updateMapLocation(myLocation);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLocation).title("My Location");
            mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
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
                if (location != null && !isLocationUpdated) {
                    currentLocation = location;
                    latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
                    mapFragment.getMapAsync(CheckoutActivity.this);
                }
            }
        });
    }

    private void setCurrentLocationAsAddress() {
        if (currentLocation != null) {
            new AlertDialog.Builder(this)
                    .setTitle("Set Current Location")
                    .setMessage("Do you want to use your current location as the address?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        Geocoder geocoder = new Geocoder(CheckoutActivity.this, Locale.getDefault());
                        try {
                            List<Address> addresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                            if (!addresses.isEmpty()) {
                                String address = addresses.get(0).getAddressLine(0);
                                etAddress.setText(address);
                                isLocationUpdated = true;
                                calculateDistance(SPECIFIC_ORIGIN, latLng);
                                Toast.makeText(CheckoutActivity.this, "Current location set as address", Toast.LENGTH_SHORT).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(CheckoutActivity.this, "Unable to get current address", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                    .show();
        } else {
            Toast.makeText(CheckoutActivity.this, "Current location not available", Toast.LENGTH_SHORT).show();
        }
    }

    private void showPlaceAutocompleteDialog() {
        PlaceAutocompleteDialogFragment dialogFragment = new PlaceAutocompleteDialogFragment();
        dialogFragment.setOnPlaceSelectedListener((placeName, latLng) -> {
            etAddress.setText(placeName);
            if (latLng != null) {
                updateMapLocation(latLng);
                calculateDistance(SPECIFIC_ORIGIN, latLng);
            }
        });
        dialogFragment.show(getSupportFragmentManager(), "place_autocomplete");
    }

    private void updateMapLocation(LatLng latLng) {
        if (mMap != null) {
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            if (!etAddress.getText().toString().isEmpty()) {
                addUserLocationMarker(latLng);
            }
            currentLocation.setLatitude(latLng.latitude);
            currentLocation.setLongitude(latLng.longitude);
            isLocationUpdated = true;
        }
    }

    private void calculateDistance(LatLng origin, LatLng destination) {
        Toast.makeText(this, "Calculating distance...", Toast.LENGTH_SHORT).show();
        DirectionsApiRequest req = DirectionsApi.newRequest(geoApiContext)
                .origin(new com.google.maps.model.LatLng(origin.latitude, origin.longitude))
                .destination(new com.google.maps.model.LatLng(destination.latitude, destination.longitude));
        req.setCallback(new PendingResult.Callback<DirectionsResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResult(DirectionsResult result) {
                runOnUiThread(() -> {
                    mMap.clear();
                    addUserLocationMarker(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()));
                    MarkerOptions shopMarker = new MarkerOptions()
                            .position(new LatLng(origin.latitude, origin.longitude))
                            .title("Shop Location");
                    mMap.addMarker(shopMarker);
                    long distanceInMeters = 0;
                    for (DirectionsLeg leg : result.routes[0].legs) {
                        distanceInMeters += leg.distance.inMeters;
                    }
                    distanceInKilometers = distanceInMeters / 1000.0;

                    tvDistance.setText("Distance: " + distanceInKilometers + " kilometers");

                    drawPolyline(result);
                });
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e("Error", e.toString());
            }
        });
    }

    private void drawPolyline(DirectionsResult result) {
        if (result.routes != null && result.routes.length > 0) {
            DirectionsRoute route = result.routes[0];

            if (route.overviewPolyline != null) {
                String encodedString = route.overviewPolyline.getEncodedPath();
                List<com.google.maps.model.LatLng> list = PolylineEncoding.decode(encodedString);
                List<LatLng> path = new ArrayList<>();

                for (com.google.maps.model.LatLng latLng : list) {
                    path.add(new LatLng(latLng.lat, latLng.lng));
                }

                Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(path));
                polyline.setColor(ContextCompat.getColor(CheckoutActivity.this, R.color.light_sea_green));
                polyline.setWidth(20);
            }
        }
    }

    private void addUserLocationMarker(LatLng latLng) {
        MarkerOptions userMarker = new MarkerOptions()
                .position(latLng)
                .icon(setIcon(this, R.drawable.combined_drawable))
                .title("Your Location");
        mMap.addMarker(userMarker);
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

    public BitmapDescriptor setIcon(Activity context, int drawableID) {
        Drawable vectorDrawable = ActivityCompat.getDrawable(context, drawableID);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);

        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}