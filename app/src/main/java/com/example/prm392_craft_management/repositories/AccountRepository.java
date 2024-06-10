package com.example.prm392_craft_management.repositories;

import com.example.prm392_craft_management.api.AuthAPIClient;
import com.example.prm392_craft_management.services.AccountService;

public class AccountRepository {
    public static AccountService getAccountService() {
        return AuthAPIClient.getClient().create(AccountService.class);
    }
}
