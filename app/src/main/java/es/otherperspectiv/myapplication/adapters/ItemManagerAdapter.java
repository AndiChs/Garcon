package es.otherperspectiv.myapplication.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.otherperspectiv.myapplication.utils.Constants;
import es.otherperspectiv.myapplication.models.Item;
import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.utils.RequestHandler;
import es.otherperspectiv.myapplication.models.User;


public class ItemManagerAdapter extends RecyclerView.Adapter<ItemManagerAdapter.ViewHolder>{

    private List<Item> itemList;
    private Context context;


    public ItemManagerAdapter(List<Item> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Item item = itemList.get(position);

        holder.tvItemDescription.setText(item.getDescription() + "- Price: " + item.getPrice());
        holder.tvItemName.setText(item.getName());

        holder.btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_DELETE_ITEM,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(!obj.getBoolean("error")){
                                        Toast.makeText(context, "The item has been deleted.", Toast.LENGTH_SHORT).show();
                                        removeItem(position);
                                    }
                                    else {
                                        Toast.makeText(context, obj.getString("message"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }

                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("name", item.getName());
                        params.put("restaurantId", Integer.toString(User.getInstance(context).getUserRestaurantId()));
                        params.put("token", User.getInstance(context).getToken());
                        return params;
                    }
                };

                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

            }
        });
    }

    public void removeItem(int position) {
        itemList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, itemList.size());
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvItemName;
        public TextView tvItemDescription;
        private Button btnDeleteItem;

        public ViewHolder(View itemView) {
            super(itemView);

            tvItemName = (TextView) itemView.findViewById(R.id.tvItemName);
            tvItemDescription = (TextView) itemView.findViewById(R.id.tvItemDescription);
            btnDeleteItem = (Button) itemView.findViewById(R.id.btnDeleteItem);
        }
    }
}
