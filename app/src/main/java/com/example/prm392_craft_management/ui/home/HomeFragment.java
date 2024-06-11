package com.example.prm392_craft_management.ui.home;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.prm392_craft_management.databinding.FragmentHomeBinding;
import com.example.prm392_craft_management.models.festival.FestivalModel;

import org.w3c.dom.Text;

import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        homeViewModel.getFestivals().observe(getViewLifecycleOwner(), new Observer<List<FestivalModel>>() {
            @Override
            public void onChanged(List<FestivalModel> festivalModels) {
                if (festivalModels != null && !festivalModels.isEmpty()) {
                    TextView festivalNamesFirst = binding.textFestivalNamesFirst;
                    TextView festivalNamesSecond = binding.textFestivalNamesSecond;
                    TextView festivalNamesThird = binding.textFestivalNamesThird;
                    TextView festivalNamesFourth = binding.textFestivalNamesFourth;

                    StringBuilder festivalNames = new StringBuilder();
                    for (FestivalModel festival : festivalModels) {
                        festivalNames.append(festival.getName()).append("\n");
                        festivalNamesFirst.append(festival.getName());
                        festivalNamesSecond.append(festival.getName());
                        festivalNamesThird.append(festival.getName());
                        festivalNamesFourth.append(festival.getName());

                    }
                    festivalNamesFirst.setText(festivalNames.toString());
                    festivalNamesSecond.setText(festivalNames.toString());
                    festivalNamesThird.setText(festivalNames.toString());
                    festivalNamesFourth.setText(festivalNames.toString());
                }
            }
        });

        homeViewModel.getError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onChanged(String error) {
                if (error != null) {
                    TextView errorTextView = binding.textError;
                    errorTextView.setText("Error: " + error);
                    Toast.makeText(getContext(), "Error: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
