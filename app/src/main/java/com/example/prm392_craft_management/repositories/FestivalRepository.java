package com.example.prm392_craft_management.repositories;

import com.example.prm392_craft_management.api.APIClient;
import com.example.prm392_craft_management.services.FestivalService;

public class FestivalRepository {
    public static FestivalService getFestivalService() {
        return APIClient.getClient().create(FestivalService.class);
    }
}
