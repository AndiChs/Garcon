package es.otherperspectiv.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import es.otherperspectiv.myapplication.models.Notification;
import es.otherperspectiv.myapplication.R;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>{

    private List<Notification> notificationList;
    private Context context;


    public NotificationAdapter(List<Notification> notificationList, Context context) {
        this.notificationList = notificationList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_notifications, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Notification item = notificationList.get(position);

        holder.tvNotificationDescription.setText(item.getDescription());
        holder.tvNotificationDate.setText(item.getCreatedAt());

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvNotificationDescription;
        public TextView tvNotificationDate;

        public ViewHolder(View itemView) {
            super(itemView);

            tvNotificationDescription = (TextView) itemView.findViewById(R.id.tvNotificationDescription);
            tvNotificationDate = (TextView) itemView.findViewById(R.id.tvNotificationDate);
        }
    }
}
