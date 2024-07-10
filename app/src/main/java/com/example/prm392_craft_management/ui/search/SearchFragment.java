package com.example.prm392_craft_management.ui.search;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.databinding.FragmentSearchBinding;
import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private SearchAdapter searchAdapter;
    private Set<String> festivalSet;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SearchViewModel searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        RecyclerView recyclerView = binding.recyclerviewSearch;
        searchAdapter = new SearchAdapter(new ArrayList<>());
        recyclerView.setAdapter(searchAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        festivalSet = new HashSet<>();

        searchViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<ProductModel> productModels) {
                searchAdapter.listProduct = productModels;
                searchAdapter.notifyDataSetChanged();
                updateChips(productModels);
            }
        });
        searchViewModel.getFilteredProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<ProductModel> productModels) {
                searchAdapter.listProduct = productModels;
                searchAdapter.notifyDataSetChanged();
                if (productModels.isEmpty()) {
                    binding.searchView.setVisibility(View.VISIBLE);
                } else {
                    binding.searchView.setVisibility(View.VISIBLE);
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

    private void updateChips(List<ProductModel> productModels) {
        binding.chipGroup.removeAllViews();
        festivalSet.clear();
        addChip("Tất cả", true);

        for (ProductModel product : productModels) {
            for (FestivalModel festival : product.getFestivals()) {
                if (!festivalSet.contains(festival.getName())) {
                    festivalSet.add(festival.getName());
                    addChip(festival.getName(), false);
                }
            }
        }
    }

    private void addChip(String festivalName, boolean isDefault) {
        Chip chip = new Chip(getContext());
        chip.setText(festivalName);
        chip.setCheckable(true);
        chip.setChecked(isDefault);

        chip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchViewModel searchViewModel = new ViewModelProvider(SearchFragment.this).get(SearchViewModel.class);
                searchViewModel.filterProductsByFestival(festivalName);
            }
        });
        binding.chipGroup.addView(chip);
    }
}
