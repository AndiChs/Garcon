package es.otherperspectiv.myapplication.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import es.otherperspectiv.myapplication.utils.Constants;
import es.otherperspectiv.myapplication.R;
import es.otherperspectiv.myapplication.utils.RequestHandler;
import es.otherperspectiv.myapplication.adapters.OrderAdapter;
import es.otherperspectiv.myapplication.models.Order;
import es.otherperspectiv.myapplication.models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaiterOrdersFragment extends Fragment {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    private List<Order> orderList;


    public WaiterOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiter_orders, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvOrders);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        orderList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_GET_ORDERS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = obj.getJSONArray("message");
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject o = jsonArray.getJSONObject(i);
                                    orderList.add(new Order(o.getString("price"), o.getString("description"), o.getString("ready_at"), o.getString("id"), o.getString("table_id")));

                                }
                                adapter = new OrderAdapter(orderList, getContext());
                                recyclerView.setAdapter(adapter);
                            }
                            else {
                                Toast.makeText(getContext(), obj.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("restaurantId", Integer.toString(User.getInstance(getContext()).getUserRestaurantId()));
                params.put("token", User.getInstance(getContext()).getToken());
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);

        super.onViewCreated(view, savedInstanceState);
    }
}
