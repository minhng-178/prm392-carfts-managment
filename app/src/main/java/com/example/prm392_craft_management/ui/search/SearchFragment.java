package com.example.prm392_craft_management.ui.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.databinding.FragmentSearchBinding;
import com.example.prm392_craft_management.models.product.ProductModel;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private SearchAdapter searchAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerviewSearch;
        searchAdapter = new SearchAdapter(new ArrayList<>());
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        searchViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<ProductModel> productModels) {
                searchAdapter.listProduct = productModels;
                searchAdapter.notifyDataSetChanged();
            }
        });
        searchViewModel.getFilteredProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @Override
            public void onChanged(List<ProductModel> productModels) {
                searchAdapter.listProduct = productModels;
                searchAdapter.notifyDataSetChanged();
                if (productModels.isEmpty()){
                    binding.textNoProducts.setVisibility(View.VISIBLE);
                } else {
                    binding.textNoProducts.setVisibility(View.GONE);
                }
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewModel.filterProducts(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchViewModel.filterProducts(newText);
                return true;
            }
        });

        return root;
    }
}
