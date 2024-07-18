package com.example.prm392_craft_management.ui.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.databinding.FragmentOrderBinding;
import com.example.prm392_craft_management.models.order.OrderModel;
import com.example.prm392_craft_management.models.order.OrderResponseModel;
import com.example.prm392_craft_management.repositories.OrderRepository;
import com.example.prm392_craft_management.services.OrderService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    private OrderViewModel orderViewModel;
    private OrderAdapter adapter;
    private RecyclerView recyclerView;
    private TextView textNoOrders;
    private Spinner spinnerAdminConfirm;

    private RelativeLayout relativeLayoutDropOff;
    private RelativeLayout relativeLayoutPickUp;
    private RelativeLayout relativeLayoutPromo;
    private RelativeLayout relativeLayoutTopUp;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerview_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        textNoOrders = root.findViewById(R.id.text_no_orders);
        textNoOrders.setVisibility(View.GONE);

        relativeLayoutDropOff = root.findViewById(R.id.relativeLayoutDropOff);
        relativeLayoutPickUp = root.findViewById(R.id.relativeLayoutPickUp);
        relativeLayoutPromo = root.findViewById(R.id.relativeLayoutPromo);
        relativeLayoutTopUp = root.findViewById(R.id.relativeLayoutTopUp);
        spinnerAdminConfirm = root.findViewById(R.id.spinnerAdminConfirm);

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        adapter = new OrderAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", "");

        setupClickListeners();

        spinnerAdminConfirm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchOrdersByStatus(3);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        if (!userId.isEmpty() || userId.matches("\\d+")) {
            fetchOrdersByStatus(3); // Default status
        } else {
            Log.e("OrderFragment", "User ID not found");
            showNoOrdersMessage();
        }

        orderViewModel.getOrderItems().observe(getViewLifecycleOwner(), orders -> {
            if (orders != null && !orders.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                textNoOrders.setVisibility(View.GONE);
                adapter.setListOrder(orders);
            } else {
                showNoOrdersMessage();
            }
        });

        return root;
    }

    private void setupClickListeners() {
        relativeLayoutDropOff.setOnClickListener(view -> fetchOrdersByStatus(3));
        relativeLayoutPickUp.setOnClickListener(view -> fetchOrdersByStatus(4));
        relativeLayoutPromo.setOnClickListener(view -> fetchOrdersByStatus(2));
        relativeLayoutTopUp.setOnClickListener(view -> fetchOrdersByStatus(0));
    }

    private void fetchOrdersByStatus(int status) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString("USER_ID", "");
        boolean isAdminConfirm = spinnerAdminConfirm.getSelectedItemPosition() == 0;

        OrderService orderService = OrderRepository.getOrderService();
        if (!userId.isEmpty() || userId.matches("\\d+")) {
            orderService.getOrdersByUserId(Integer.parseInt(userId), "created_at", status, isAdminConfirm).enqueue(new Callback<OrderResponseModel>() {
                @Override
                public void onResponse(@NonNull Call<OrderResponseModel> call, @NonNull Response<OrderResponseModel> response) {
                    if (response.isSuccessful()) {
                        OrderResponseModel orderResponse = response.body();
                        if (orderResponse != null && orderResponse.getOrders() != null) {
                            List<OrderModel> orders = orderResponse.getOrders();
                            orderViewModel.setOrderItems(orders);
                            updateSelectedStatusUI(status);
                        } else {
                            showNoOrdersMessage();
                        }
                    } else {
                        Log.e("OrderFragment", "Error fetching orders: " + response.message());
                        showNoOrdersMessage();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OrderResponseModel> call, @NonNull Throwable t) {
                    Log.e("OrderFragment", "Failed to get orders", t);
                    showNoOrdersMessage();
                }
            });
        } else {
            Log.e("OrderFragment", "User ID not found");
            showNoOrdersMessage();
        }
    }

    private void updateSelectedStatusUI(int status) {
        relativeLayoutDropOff.findViewById(R.id.image_1).setBackgroundResource(R.drawable.bg_item_choose_services);
        relativeLayoutPickUp.findViewById(R.id.image_2).setBackgroundResource(R.drawable.bg_item_choose_services);
        relativeLayoutPromo.findViewById(R.id.image_3).setBackgroundResource(R.drawable.bg_item_choose_services);
        relativeLayoutTopUp.findViewById(R.id.image_4).setBackgroundResource(R.drawable.bg_item_choose_services);

        switch (status) {
            case 0:
                relativeLayoutTopUp.findViewById(R.id.image_4).setBackgroundResource(R.drawable.bg_item_selected);
                break;
            case 4:
                relativeLayoutPickUp.findViewById(R.id.image_2).setBackgroundResource(R.drawable.bg_item_selected);
                break;
            case 2:
                relativeLayoutPromo.findViewById(R.id.image_3).setBackgroundResource(R.drawable.bg_item_selected);
                break;
            case 3:
                relativeLayoutDropOff.findViewById(R.id.image_1).setBackgroundResource(R.drawable.bg_item_selected);
                break;
        }
    }

    private void showNoOrdersMessage() {
        recyclerView.setVisibility(View.GONE);
        textNoOrders.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}

