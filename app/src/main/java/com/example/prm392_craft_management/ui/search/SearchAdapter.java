package com.example.prm392_craft_management.ui.search;

import android.content.Intent;
import android.text.TextUtils;
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
import com.example.prm392_craft_management.models.festival.FestivalModel;
import com.example.prm392_craft_management.models.image.ImageModel;
import com.example.prm392_craft_management.models.product.ProductModel;
import com.example.prm392_craft_management.ui.details.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    List<ProductModel> listProduct;

    public SearchAdapter(List<ProductModel> listProduct) {
        this.listProduct = listProduct;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_search, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductModel list = listProduct.get(position);
        holder.textName.setText(list.getName());
        holder.textPrice.setText(String.valueOf(list.getPrice()));

        List<String> festivalNames = new ArrayList<>();
        for (FestivalModel festival : list.getFestivals()) {
            festivalNames.add(festival.getName());
        }
        String festivalsText = TextUtils.join(", ", festivalNames);
        holder.textFestival.setText(festivalsText);

        if (!list.getImages().isEmpty()) {
            ImageModel imageModel = list.getImages().get(0);
            String imageUrl = imageModel.getUrl();
            Glide.with(holder.imageSearch.getContext()).load(imageUrl).into(holder.imageSearch);
        }

        holder.containerSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductDetailActivity.class);
                intent.putExtra("PRODUCT_ID", list.getId());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView containerSearch;
        ImageView imageSearch;
        TextView textPrice, textName, textFestival;

        public ViewHolder(View itemView) {
            super(itemView);

            containerSearch = itemView.findViewById(R.id.container_search);
            imageSearch = itemView.findViewById(R.id.item_search_image);
            textName = itemView.findViewById(R.id.item_search_name);
            textPrice = itemView.findViewById(R.id.item_search_price);
            textFestival = itemView.findViewById(R.id.item_search_festival);
        }
    }
}
