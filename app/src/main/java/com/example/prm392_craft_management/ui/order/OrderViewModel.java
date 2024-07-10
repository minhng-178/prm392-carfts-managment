package com.example.prm392_craft_management.ui.order;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prm392_craft_management.models.order.OrderModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class OrderViewModel extends ViewModel {
    private final MutableLiveData<List<OrderModel>> orderItems = new MutableLiveData<>(new ArrayList<>());

    public LiveData<List<OrderModel>> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderModel> orders) {
        orderItems.setValue(orders);
    }

    public void addOrderItem(OrderModel order) {
        List<OrderModel> currentOrders = orderItems.getValue();
        if (currentOrders != null) {
            currentOrders.add(order);
            orderItems.setValue(currentOrders);
        }
    }

    public void updateOrderItem(OrderModel order) {
        List<OrderModel> currentOrders = orderItems.getValue();
        if (currentOrders != null) {
            for (int i = 0; i < currentOrders.size(); i++) {
                if (currentOrders.get(i).getId() == order.getId()) {
                    currentOrders.set(i, order);
                    orderItems.setValue(currentOrders);
                    return;
                }
            }
        }
    }

    public void removeOrderItem(int orderId) {
        List<OrderModel> currentOrders = orderItems.getValue();
        if (currentOrders != null) {
            for (int i = 0; i < currentOrders.size(); i++) {
                if (currentOrders.get(i).getId() == orderId) {
                    currentOrders.remove(i);
                    orderItems.setValue(currentOrders);
                    return;
                }
            }
        }
    }

    public void refreshOrders(List<OrderModel> newOrders) {
        orderItems.setValue(newOrders);
    }

    public LiveData<List<OrderModel>> getOrdersByStatus(int status) {
        MutableLiveData<List<OrderModel>> ordersByStatus = new MutableLiveData<>(new ArrayList<>());
        List<OrderModel> currentOrders = orderItems.getValue();
        if (currentOrders != null) {
            List<OrderModel> filteredOrders = currentOrders.stream()
                    .filter(order -> order.getStatus() == status)
                    .collect(Collectors.toList());
            ordersByStatus.setValue(filteredOrders);
        }
        return ordersByStatus;
    }
}
