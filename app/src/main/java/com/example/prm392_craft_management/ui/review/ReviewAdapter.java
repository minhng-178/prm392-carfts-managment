package com.example.prm392_craft_management.ui.review;

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

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    List<ProductModel> listProduct;

    public ReviewAdapter(List<ProductModel> listProduct) {
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_confirm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        ProductModel list = listProduct.get(position);
        holder.textName.setText(list.getName());
        holder.textPrice.setText(String.valueOf(list.getPrice()));

        if (!list.getImages().isEmpty()) {
            ImageModel imageModel = list.getImages().get(0);
            String imageUrl = imageModel.getImage_url();
            Glide.with(holder.imageConfirm.getContext())
                    .load(imageUrl)
                    .into(holder.imageConfirm);
        }
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView containerConfirm;
        ImageView imageConfirm;
        TextView textName, textPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            containerConfirm = itemView.findViewById(R.id.container_confirm);
            imageConfirm = itemView.findViewById(R.id.item_confirm_image);
            textName = itemView.findViewById(R.id.item_confirm_name);
            textPrice = itemView.findViewById(R.id.item_confirm_price);
        }
    }
}
