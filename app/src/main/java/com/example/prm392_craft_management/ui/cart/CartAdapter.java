package com.example.prm392_craft_management.ui.cart;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.cart.CartModel;
import com.example.prm392_craft_management.models.image.ImageModel;
import com.example.prm392_craft_management.ui.details.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final List<CartModel> cartList;
    private boolean[] itemSelected;
    private final List<Integer> selectedCartIds = new ArrayList<>();


    public CartAdapter(List<CartModel> cartList) {
        this.cartList = cartList;
        itemSelected = new boolean[cartList.size()];
    }

    public List<CartModel> getCartList() {
        return cartList;
    }

    public boolean[] getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int position, boolean isSelected) {
        itemSelected[position] = isSelected;
        if (isSelected) {
            int cartId = cartList.get(position).getCart_id();
            selectedCartIds.add(cartId);
        } else {
            int cartId = cartList.get(position).getCart_id();
            selectedCartIds.remove(Integer.valueOf(cartId));
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container_cart;
        TextView tvCartItemName, tvCartItemPrice, tvCartItemQuantity;
        ImageView ivCartItemImage;

        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCartItemImage = itemView.findViewById(R.id.ivCartItem);
            tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
            tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvCartItemQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            container_cart = itemView.findViewById(R.id.container_cart);
            checkBox = itemView.findViewById(R.id.checkbox_select_item);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CartModel cartItem = cartList.get(position);
        holder.tvCartItemName.setText(cartItem.getName());

        holder.tvCartItemPrice.setText(String.valueOf(cartItem.getPrice()));
        holder.tvCartItemQuantity.setText(String.valueOf(cartItem.getAmount()));

        if (!cartItem.getImages().isEmpty()) {
            ImageModel imageModel = cartItem.getImages().get(0);
            String imageUrl = imageModel.getUrl();
            Glide.with(holder.ivCartItemImage.getContext()).load(imageUrl).into(holder.ivCartItemImage);
        }

        holder.checkBox.setChecked(itemSelected[position]);
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            itemSelected[position] = isChecked;
            ((CartActivity) holder.itemView.getContext()).checkIfAllSelected();
            ((CartActivity) holder.itemView.getContext()).updateCancelButtonState();
            ((CartActivity) holder.itemView.getContext()).onCheckboxStateChanged();
        });

        holder.container_cart.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), ProductDetailActivity.class);
            intent.putExtra("PRODUCT_ID", cartItem.getProduct_id());
            view.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public void updateCartList(List<CartModel> newCartList) {
        cartList.clear();
        cartList.addAll(newCartList);
        itemSelected = new boolean[newCartList.size()];
    }

    public boolean areAllItemsSelected() {
        for (boolean isSelected : itemSelected) {
            if (!isSelected) {
                return false;
            }
        }
        return true;
    }

    public boolean isItemSelected(int position) {
        return itemSelected[position];
    }

    public boolean areAnyItemsSelected() {
        for (boolean isSelected : itemSelected) {
            if (isSelected) {
                return true;
            }
        }
        return false;
    }
}

