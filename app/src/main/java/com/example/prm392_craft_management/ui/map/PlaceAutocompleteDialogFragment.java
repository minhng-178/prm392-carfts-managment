package com.example.prm392_craft_management.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.prm392_craft_management.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class PlaceAutocompleteDialogFragment extends DialogFragment {
    private OnPlaceSelectedListener listener;

    public interface OnPlaceSelectedListener {
        void onPlaceSelected(String placeName, LatLng placeLatLng);
    }

    public void setOnPlaceSelectedListener(OnPlaceSelectedListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_place_autocomplete_dialog, container, false);

        Places.initialize(getContext(), getString(R.string.map_api_key));

        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                if (listener != null) {
                    listener.onPlaceSelected(place.getName(), place.getLatLng());
                }
                dismiss();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(), "An error occurred: " + status, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}