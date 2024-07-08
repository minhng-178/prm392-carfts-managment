package com.example.prm392_craft_management.ui.order;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recyclerView = root.findViewById(R.id.recyclerview_order);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        textNoOrders = root.findViewById(R.id.text_no_orders);
        textNoOrders.setVisibility(View.GONE);  // Ẩn TextView khi chưa có đơn hàng nào

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        // Khởi tạo Adapter và gắn vào RecyclerView
        adapter = new OrderAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Lấy userId từ SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
        int userId = sharedPreferences.getInt("USER_ID", -1);
        Log.d("OrderFragment", "Retrieved userId: " + userId);

        OrderService orderService = OrderRepository.getOrderService();
        if (userId != -1) {
            orderService.getOrdersByUserId(23).enqueue(new Callback<OrderResponseModel>() {
                @Override
                public void onResponse(Call<OrderResponseModel> call, Response<OrderResponseModel> response) {
                    if (response.isSuccessful()) {
                        OrderResponseModel orderResponse = response.body();
                        if (orderResponse != null && orderResponse.getOrders() != null) {
                            List<OrderModel> orders = orderResponse.getOrders();
                            Log.d("OrderFragment", "Number of orders retrieved: " + orders.size());
                            orderViewModel.setOrderItems(orders);
                        } else {
                            Log.d("OrderFragment", "No orders retrieved or orders list is null");
                            showNoOrdersMessage();
                        }
                    } else {
                        Log.e("OrderFragment", "Error fetching orders: " + response.message());
                        showNoOrdersMessage();
                    }
                }

                @Override
                public void onFailure(Call<OrderResponseModel> call, Throwable t) {
                    Log.e("OrderFragment", "Failed to get orders", t);
                    showNoOrdersMessage();
                }
            });
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
