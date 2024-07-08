package com.example.prm392_craft_management.ui.order;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.order.OrderModel;
import com.example.prm392_craft_management.models.product.ProductModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {
    private List<OrderModel> listOrder;

    public OrderAdapter(List<OrderModel> listOrder) {
        this.listOrder = listOrder;
        Log.d("OrderAdapter", "OrderAdapter initialized with orders: " + listOrder.size());
    }

    public void setListOrder(List<OrderModel> listOrder) {
        this.listOrder = listOrder;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        OrderModel order = listOrder.get(position);
        holder.bind(order);
    }

    @Override
    public int getItemCount() {
        return listOrder != null ? listOrder.size() : 0;
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewProduct;
        private final TextView textViewProductName;
        private final TextView textViewProductPrice;
        private final TextView textViewTotalPrice;
        private final TextView textViewOrderStatus;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus);
        }

        public void bind(OrderModel order) {
            if (order.getProducts() != null && !order.getProducts().isEmpty()) {
                ProductModel product = order.getProducts().get(0);

                Log.d("OrderAdapter", "Order ID: " + order.getId());
                Log.d("OrderAdapter", "Product Name: " + product.getName());
                Log.d("OrderAdapter", "Order Status: " + order.getStatus());

                textViewProductName.setText(product.getName());
                textViewProductPrice.setText(String.format("Giá: %s", product.getPrice()));
                textViewTotalPrice.setText(String.format("Tổng: %s", order.getTotalPrice()));
                textViewOrderStatus.setText(getOrderStatus(order.getStatus()));

                if (product.getImages() != null && !product.getImages().isEmpty()) {
                    Log.d("OrderAdapter", "Product Image URL: " + product.getImages().get(0).getUrl());
                    Glide.with(itemView.getContext())
                            .load(product.getImages().get(0).getUrl())
                            .placeholder(R.drawable.avatar)
                            .into(imageViewProduct);
                } else {
                    Log.d("OrderAdapter", "Product has no images, setting default image");
                    imageViewProduct.setImageResource(R.drawable.avatar);
                }
            } else {
                Log.d("OrderAdapter", "Order has no products");
            }
        }

        private String getOrderStatus(int status) {
            switch (status) {
                case 0:
                    return "Hủy";
                case 1:
                    return "Processing";
                case 2:
                    return "Confirm";
                case 3:
                    return "Đã thanh toán";
                case 4:
                    return "Ship code";
                case 5:
                    return "Hide";
                case 6:
                    return "Refunding";
                case 7:
                    return "Refunded";
                default:
                    return "Unknown";
            }
        }
    }
}
