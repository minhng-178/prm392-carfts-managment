package com.example.prm392_craft_management.ui.order;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.order.OrderModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.example.prm392_craft_management.ui.details.OrderDetailActivity;
import com.example.prm392_craft_management.ui.details.ProductDetailActivity;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<OrderModel> listOrder;

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public OrderAdapter(List<OrderModel> listOrder) {
        this.listOrder = listOrder;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListOrder(List<OrderModel> listOrder) {
        this.listOrder = listOrder;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return listOrder.get(position) == null ? TYPE_HEADER : TYPE_ITEM;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
            return new OrderViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof OrderViewHolder) {
            OrderModel order = listOrder.get(position);
            ((OrderViewHolder) holder).bind(order);
        } else {
            ((HeaderViewHolder) holder).bind(getHeaderTitle(position));
        }
    }

    @Override
    public int getItemCount() {
        return listOrder != null ? listOrder.size() : 0;
    }

    private String getHeaderTitle(int position) {
        // Implement your logic to return header title based on position
        return "Header Title";
    }

    static class OrderViewHolder extends RecyclerView.ViewHolder {
        private final CardView containerOrder;
        private final ImageView imageViewProduct;
        private final TextView textViewProductName;
        private final TextView textViewProductPrice;
        private final TextView textViewTotalPrice;
        private final TextView textViewOrderStatus;
        private final TextView textViewAmount;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            containerOrder = itemView.findViewById(R.id.container_order);
            imageViewProduct = itemView.findViewById(R.id.imageViewProduct);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            textViewTotalPrice = itemView.findViewById(R.id.textViewTotalPrice);
            textViewOrderStatus = itemView.findViewById(R.id.textViewOrderStatus);
            textViewAmount = itemView.findViewById(R.id.textViewAmount);
        }

        public void bind(OrderModel order) {
            if (order.getProducts() != null && !order.getProducts().isEmpty()) {
                ProductModel product = order.getProducts().get(0);
                textViewProductName.setText(product.getName());
                textViewProductPrice.setText(String.valueOf(order.getTotalProductPrice()));
                textViewTotalPrice.setText(String.valueOf(order.getTotalPrice()));
                textViewOrderStatus.setText(getStatusText(order.getStatus()));
                textViewOrderStatus.setTextColor(getStatusColor(order.getStatus()));
                textViewAmount.setText(String.valueOf("x" + product.getAmount()));

                if (product.getImages() != null && !product.getImages().isEmpty()) {
                    Glide.with(itemView.getContext())
                            .load(product.getImages().get(0).getUrl())
                            .placeholder(R.drawable.empty_cart)
                            .into(imageViewProduct);
                } else {
                    imageViewProduct.setImageResource(R.drawable.empty_cart);
                }

                containerOrder.setOnClickListener(v -> {
                    Intent intent = new Intent(v.getContext(), OrderDetailActivity.class);
                    intent.putExtra("ORDER_ID", order.getId());
                    v.getContext().startActivity(intent);
                });
            }
        }

        private String getStatusText(int status) {
            switch (status) {
                case 0:
                    return "Hủy";
                case 1:
                    return "Processing";
                case 2:
                    return "Confirm";
                case 3:
                    return "Đã thanh toán (Online)";
                case 4:
                    return "Ship Code";
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

        private int getStatusColor(int status) {
            Context context = itemView.getContext();
            switch (status) {
                case 0:
                    return ContextCompat.getColor(context, R.color.status_cancelled);
                case 1:
                    return ContextCompat.getColor(context, R.color.status_processing);
                case 2:
                    return ContextCompat.getColor(context, R.color.status_confirmed);
                case 3:
                    return ContextCompat.getColor(context, R.color.status_paid);
                case 4:
                    return ContextCompat.getColor(context, R.color.status_ship_code);
                case 5:
                    return ContextCompat.getColor(context, R.color.status_hidden);
                case 6:
                    return ContextCompat.getColor(context, R.color.status_refunding);
                case 7:
                    return ContextCompat.getColor(context, R.color.status_refunded);
                default:
                    return ContextCompat.getColor(context, R.color.status_unknown);
            }
        }
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHeader;

        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewHeader = itemView.findViewById(R.id.textViewHeader);
        }

        public void bind(String headerTitle) {
            textViewHeader.setText(headerTitle);
        }
    }
}
