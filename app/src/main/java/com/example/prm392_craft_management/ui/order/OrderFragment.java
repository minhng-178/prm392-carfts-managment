package com.example.prm392_craft_management.ui.order;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_craft_management.databinding.FragmentOrderBinding;

public class OrderFragment extends Fragment {
    private FragmentOrderBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        OrderViewModel orderViewModel = new ViewModelProvider(this).get(OrderViewModel.class);
        binding = FragmentOrderBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        final TextView textView = binding.textOrder;
        orderViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        return root;
    }

}
