package com.example.prm392_craft_management.ui.order;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.order.OrderModel;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    List<OrderModel> listOrder;

    public OrderAdapter(List<OrderModel> listOrder) {
        this.listOrder = listOrder;
    }

    public void setListOrder(List<OrderModel> listOrder) {
        this.listOrder = listOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderModel list = listOrder.get(position);

        holder.imageOrder.setImageResource(list.getImage());
        holder.textName.setText(list.getName());
        holder.textStatusOrder.setText(list.getStatus_order());
        holder.containerRecommended.setOnClickListener(view -> {
//                view.getContext().startActivity(new Intent(view.getContext(), HomeDetailActivity.class));
            Toast.makeText(view.getContext(), "Clicked!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return listOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView containerRecommended;
        ImageView imageOrder;
        TextView textName, textStatusOrder;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            containerRecommended = itemView.findViewById(R.id.container_order);
            imageOrder           = itemView.findViewById(R.id.item_order_image);
            textName             = itemView.findViewById(R.id.item_order_name);
            textStatusOrder      = itemView.findViewById(R.id.item_order_status_order);
        }
    }
}
