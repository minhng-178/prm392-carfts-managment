package com.example.prm392_craft_management.ui.order;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.image.ImageModel;
import com.example.prm392_craft_management.models.product.ProductModel;

import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    List<ProductModel> listProduct;

    public OrderDetailAdapter(List<ProductModel> listProduct) {
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public OrderDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_orderproducts, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.ViewHolder holder, int position) {
        ProductModel list = listProduct.get(position);
        holder.textName.setText(list.getName());
        holder.textPrice.setText(String.valueOf(list.getPrice()));
        holder.textAmount.setText("x" + String.valueOf(list.getAmount()));

        if (!list.getImages().isEmpty()) {
            ImageModel imageModel = list.getImages().get(0);

            String imageUrl = imageModel.getUrl();
            Glide.with(holder.imageOrder.getContext())
                    .load(imageUrl)
                    .placeholder(R.drawable.empty_cart)
                    .into(holder.imageOrder);
        }
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView containerOrder;
        ImageView imageOrder;
        TextView textName, textPrice, textAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            containerOrder = itemView.findViewById(R.id.container_orderproducts);
            imageOrder = itemView.findViewById(R.id.item_order_image);
            textName = itemView.findViewById(R.id.item_order_name);
            textPrice = itemView.findViewById(R.id.item_order_price);
            textAmount = itemView.findViewById(R.id.item_order_amount);
        }
    }
}
