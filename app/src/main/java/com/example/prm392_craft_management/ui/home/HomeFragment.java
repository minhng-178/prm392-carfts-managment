package com.example.prm392_craft_management.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.databinding.FragmentHomeBinding;
import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private Context mContext;
    private FragmentHomeBinding binding;
    private HomeAdapter homeAdapter;
    private CarouselAdapter carouselAdapter;
    private NavController navController;
    private final Handler sliderHandler = new Handler();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ViewPager2 viewPager2 = binding.viewpagerCarousel;
        List<String> imageUrls = new ArrayList<>();
        carouselAdapter = new CarouselAdapter(imageUrls, mContext);
        viewPager2.setAdapter(carouselAdapter);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        RecyclerView recyclerView = binding.recyclerviewRecommended;
        homeAdapter = new HomeAdapter(new ArrayList<>());
        recyclerView.setAdapter(homeAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setNestedScrollingEnabled(true);

        homeViewModel.getProducts().observe(getViewLifecycleOwner(), new Observer<List<ProductModel>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<ProductModel> productModels) {
                homeAdapter.listProduct = productModels;
                homeAdapter.notifyDataSetChanged();
            }
        });

        homeViewModel.getCarouselImages().observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChanged(List<String> imageUrls) {
                carouselAdapter.imageUrls = imageUrls;
                carouselAdapter.notifyDataSetChanged();
            }
        });

        navController = NavHostFragment.findNavController(this);
        binding.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_navigation_home_to_navigation_search);
            }
        });
        return root;
    }

    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            if (binding.viewpagerCarousel.getCurrentItem() < carouselAdapter.getItemCount() - 1) {
                binding.viewpagerCarousel.setCurrentItem(binding.viewpagerCarousel.getCurrentItem() + 1);
            } else {
                binding.viewpagerCarousel.setCurrentItem(0);
            }
        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }
}

