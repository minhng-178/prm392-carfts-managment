package com.example.prm392_craft_management.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.databinding.FragmentOrderBinding;
import com.example.prm392_craft_management.models.order.OrderModel;
import com.example.prm392_craft_management.R;

import java.util.ArrayList;
import java.util.List;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;
    private OrderViewModel orderViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);

        List<OrderModel> orderModels = new ArrayList<>();
        orderModels.add(new OrderModel(R.drawable.avatar, "Order 1", "Processing"));
        orderModels.add(new OrderModel(R.drawable.avatar, "Order 2", "Delivered"));
        orderModels.add(new OrderModel(R.drawable.avatar, "Order 3", "Cancelled"));

        orderViewModel.setOrderItems(orderModels);

        OrderAdapter adapter = new OrderAdapter(orderModels);
        recyclerView.setAdapter(adapter);

        orderViewModel.getOrderItems().observe(getViewLifecycleOwner(), orderItems -> {
            adapter.setListOrder(orderItems);
            adapter.notifyDataSetChanged();
        });

        return root;
    }
}

