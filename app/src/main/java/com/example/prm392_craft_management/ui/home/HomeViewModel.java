package com.example.prm392_craft_management.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.festival.FestivalResponseModel;
import com.example.prm392_craft_management.repositories.FestivalRepository;
import com.example.prm392_craft_management.services.FestivalService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mError;
    private final MutableLiveData<List<FestivalModel>> mFestivals;

    public HomeViewModel() {
        mError = new MutableLiveData<>();
        mFestivals = new MutableLiveData<>();
        loadFestivals();
    }

    public LiveData<List<FestivalModel>> getFestivals() {
        return mFestivals;
    }

    public LiveData<String> getError() {
        return mError;
    }

    private void loadFestivals() {
        FestivalService service = FestivalRepository.getFestivalService();
        service.getAllFestivals(1, "created_at:asc").enqueue(new Callback<FestivalResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<FestivalResponseModel> call, @NonNull Response<FestivalResponseModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    mFestivals.postValue(response.body().getData());
                } else {
                    Log.d("API Response", "Response Code: " + response.code());
                    Log.d("API Message", "Response Message: " + response.message());

                    mError.setValue("Error: " + response.code() + " " + response.message());
                }
            }

            @Override
            public void onFailure(@NonNull Call<FestivalResponseModel> call, @NonNull Throwable t) {
                mFestivals.postValue(null);
                mError.setValue(t.getMessage());
            }
        });
    }
};

