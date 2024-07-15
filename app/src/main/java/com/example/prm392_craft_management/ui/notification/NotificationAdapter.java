package com.example.prm392_craft_management.ui.notification;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_craft_management.R;
import com.example.prm392_craft_management.models.notification.NotificationModel;
import com.example.prm392_craft_management.utils.DateUtils;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    private final List<NotificationModel> listNotifications;

    public NotificationAdapter(List<NotificationModel> listNotifications) {
        this.listNotifications = listNotifications;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        NotificationModel notification = listNotifications.get(position);
        holder.tvNotificationMessage.setText(notification.getMessage());
        String createdAt = notification.getCreated_at();
        String formattedDate = DateUtils.formatDate(createdAt);
        holder.tvNotificationDate.setText(formattedDate);
    }

    @Override
    public int getItemCount() {
        return listNotifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView containerNotification;
        TextView tvNotificationMessage, tvNotificationDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            containerNotification = itemView.findViewById(R.id.container_notification);
            tvNotificationMessage = itemView.findViewById(R.id.item_message);
            tvNotificationDate = itemView.findViewById(R.id.item_date);
        }
    }
}
