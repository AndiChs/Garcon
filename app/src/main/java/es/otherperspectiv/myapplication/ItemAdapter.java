package es.otherperspectiv.myapplication;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder>{

    private List<Item> itemList;
    private Context context;


    public ItemAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_add_order, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Item item = itemList.get(position);

        holder.tvItemDescription.setText(item.getDescription() + " - Price: " + item.getPrice());
        holder.tvItemName.setText(item.getName());

        holder.btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User.getInstance(view.getContext()).addItemToOrder(item.getName(), item.getPrice());
                Toast.makeText(context, "You added " + item.getName() + " to the basket.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvItemName;
        public TextView tvItemDescription;
        private Button btnAddItem;

        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemDescription = (TextView) itemView.findViewById(R.id.tvItemDescription);
            btnAddItem = (Button) itemView.findViewById(R.id.btnAcceptRequest);
        }
    }
}
