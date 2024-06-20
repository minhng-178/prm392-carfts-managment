package com.example.prm392_craft_management.ui.cart;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.cart.CartModel;
import com.example.prm392_craft_management.models.cart.CartResponseModel;
import com.example.prm392_craft_management.models.image.ImageModel;
import com.example.prm392_craft_management.repositories.CartRepository;
import com.example.prm392_craft_management.services.CartService;
import com.example.prm392_craft_management.ui.details.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private final List<CartModel> cartList;
    private boolean[] itemSelected;

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
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout container_cart;
        TextView tvCartItemName, tvCartItemPrice, tvCartItemQuantity;
        ImageView ivCartItemImage, ivDeleteItem;

        CheckBox checkBox;

        public ViewHolder(View itemView) {
            super(itemView);
            ivCartItemImage = itemView.findViewById(R.id.ivCartItem);
            tvCartItemName = itemView.findViewById(R.id.tvCartItemName);
            tvCartItemPrice = itemView.findViewById(R.id.tvCartItemPrice);
            tvCartItemQuantity = itemView.findViewById(R.id.tvCartItemQuantity);
            ivDeleteItem = itemView.findViewById(R.id.ivDeleteItem);
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

        holder.ivDeleteItem.setOnClickListener(view -> {
            int currentPosition = holder.getAdapterPosition();

            if (currentPosition != RecyclerView.NO_POSITION) {
                AlertDialog.Builder builder = new AlertDialog.Builder(holder.itemView.getContext());
                builder.setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton("Yes", (dialog, id) -> {
                            SharedPreferences sharedPreferences = holder.itemView.getContext().getSharedPreferences("User_Info", Context.MODE_PRIVATE);
                            String userId = sharedPreferences.getString("USER_ID", "");
                            List<CartModel.CartProduct> cartProducts = new ArrayList<>();
                            cartProducts.add(new CartModel.CartProduct(cartList.get(currentPosition).getProduct_id(), 0));
                            CartModel cart = new CartModel(Integer.parseInt(userId), cartProducts);

                            CartService cartService = CartRepository.getCartService();
                            cartService.postUserCart(cart).enqueue(new Callback<CartResponseModel>() {
                                @Override
                                public void onResponse(@NonNull Call<CartResponseModel> call, @NonNull Response<CartResponseModel> response) {
                                    if (response.isSuccessful()) {
                                        cartList.remove(currentPosition);
                                        notifyItemRemoved(currentPosition);
                                        Toast.makeText(holder.itemView.getContext(), "Item removed from cart", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(holder.itemView.getContext(), "Failed to remove item", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onFailure(@NonNull Call<CartResponseModel> call, @NonNull Throwable throwable) {
                                    Toast.makeText(holder.itemView.getContext(), "Error while removing item", Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("No", (dialog, id) -> {
                            dialog.dismiss();
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
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

