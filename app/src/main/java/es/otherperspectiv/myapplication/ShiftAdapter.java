package es.otherperspectiv.myapplication;


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

public class ShiftAdapter extends RecyclerView.Adapter<ShiftAdapter.ViewHolder>{

    private List<Shift> shiftList;
    private Context context;


    public ShiftAdapter(List<Shift> orderList, Context context) {
        this.shiftList = orderList;
        this.context = context;
    }

    @Override
    public ShiftAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_shifts, parent, false);
        return new ShiftAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ShiftAdapter.ViewHolder holder, int position) {
        final Shift shift = shiftList.get(position);

        holder.tvShiftUsername.setText("Name: " + shift.getUsername());
        holder.tvShiftDate.setText("Date: " + shift.getDateStart() + " | Working hours: " + shift.getWorkingHours());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_DELETE_SHIFT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    if(!obj.getBoolean("error")){
                                        Toast.makeText(context, "The shift has been deleted.", Toast.LENGTH_SHORT).show();
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
                        params.put("date", shift.getDateStart());
                        params.put("working_hours", Integer.toString(shift.getWorkingHours()));
                        return params;
                    }
                };

                RequestHandler.getInstance(context).addToRequestQueue(stringRequest);

            }
        });
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tvShiftUsername;
        public TextView tvShiftDate;
        private Button btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            tvShiftUsername = (TextView) itemView.findViewById(R.id.tvShiftUsername);
            tvShiftDate = (TextView) itemView.findViewById(R.id.tvShiftDate);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);
        }
    }
}
