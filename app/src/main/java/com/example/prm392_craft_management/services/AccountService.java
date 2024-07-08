package com.example.prm392_craft_management.services;


import com.example.prm392_craft_management.models.account.AccountModel;
import com.example.prm392_craft_management.models.account.AccountRequestModel;
import com.example.prm392_craft_management.models.account.AccountResponseModel;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AccountService {
    String ACCOUNT = "auth";

    @POST(ACCOUNT + "/signUp")
    Call<AccountResponseModel> register(@Body AccountRequestModel registerAccount);

    @POST(ACCOUNT + "/signIn")
    Call<AccountResponseModel> login(@Body AccountRequestModel loginAccount);

    @POST(ACCOUNT + "/google-sign-in")
    Call<AccountResponseModel> loginWithGoogle(@Body String idToken);
}
