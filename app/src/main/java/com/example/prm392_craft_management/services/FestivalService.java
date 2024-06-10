package com.example.prm392_craft_management.services;

import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.festival.FestivalResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface FestivalService {
    String FESTIVAL = "festival";

    @GET(FESTIVAL)
    Call<FestivalResponseModel> getAllFestivals(
            @Query("status") int status,
            @Query("sort") String sort
    );

    @GET(FESTIVAL + "/{id}")
    Call<FestivalModel> getFestivalById(@Path("id") int id);
}
