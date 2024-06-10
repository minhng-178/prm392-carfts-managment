package com.example.prm392_craft_management.services;


import com.example.prm392_craft_management.models.account.AccountModel;
import com.example.prm392_craft_management.models.account.AccountResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {
    String ACCOUNT = "auth";

    @POST(ACCOUNT + "/signUp")
    Call<AccountResponseModel> register(@Body AccountModel loginAccount);

    @POST(ACCOUNT + "/signIn")
    Call<AccountResponseModel> login(@Body AccountModel registerAccount);
}
