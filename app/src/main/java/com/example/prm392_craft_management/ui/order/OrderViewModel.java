package com.example.prm392_craft_management.ui.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_craft_management.models.order.OrderModel;

import java.util.List;

public class OrderViewModel extends ViewModel {
    private final MutableLiveData<List<OrderModel>> orderItems;

    public OrderViewModel() {
        orderItems = new MutableLiveData<>();
    }

    public void setOrderItems(List<OrderModel> orderModels) {
        orderItems.setValue(orderModels);
    }

    public LiveData<List<OrderModel>> getOrderItems() {
        return orderItems;
    }
}
