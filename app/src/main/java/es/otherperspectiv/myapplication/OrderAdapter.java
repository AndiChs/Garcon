package es.otherperspectiv.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;


public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {
    private List<Order> orderList;
    private Context context;


    public OrderAdapter(List<Order> orderList, Context context) {
        this.orderList = orderList;
        this.context = context;
    }

    @Override
    public OrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_orders, parent, false);
        return new OrderAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(OrderAdapter.ViewHolder holder, int position) {
        final Order order = orderList.get(position);

        holder.tvOrderId.setText("Order ID: #" + order.getId());
        holder.tvOrderDescription.setText("Order Description: " + order.getDescription());
        holder.tvOrderPrice.setText("Order price: " + order.getPrice());
        holder.tvOrderReadyAt.setText("Ready at: " +order.getReadyAt());
        holder.tvOrderTable.setText("Table: " +order.getTableId());

    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvOrderPrice;
        public TextView tvOrderDescription;
        public TextView tvOrderReadyAt;
        public TextView tvOrderId;
        public TextView tvOrderTable;

        public ViewHolder(View itemView) {
            super(itemView);

            tvOrderPrice = (TextView) itemView.findViewById(R.id.tvOrderPrice);
            tvOrderDescription = (TextView) itemView.findViewById(R.id.tvOrderDescription);
            tvOrderReadyAt = (TextView) itemView.findViewById(R.id.tvOrderReadyAt);
            tvOrderId = (TextView) itemView.findViewById(R.id.tvOrderId);
            tvOrderTable = (TextView) itemView.findViewById(R.id.tvOrderTable);

        }
    }
}
