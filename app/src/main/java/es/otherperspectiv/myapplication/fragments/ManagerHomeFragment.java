package es.otherperspectiv.myapplication.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
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
import es.otherperspectiv.myapplication.adapters.ShiftAdapter;
import es.otherperspectiv.myapplication.models.Shift;
import es.otherperspectiv.myapplication.models.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class ManagerHomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private CalendarView calendarView;

    private List<Shift> shiftList;


    public ManagerHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rvShifts);
        calendarView = (CalendarView) view.findViewById(R.id.calendarView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        shiftList = new ArrayList<>();

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_RESTAURANT_SHIFTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = obj.getJSONArray("message");
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject o = jsonArray.getJSONObject(i);
                                    shiftList.add(new Shift(o.getString("date_start"), o.getInt("working_hours"), o.getString("name")));

                                }
                                adapter = new ShiftAdapter(shiftList, getContext());
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

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                List<Shift> shiftList2 = new ArrayList<>();
                i1++;
                for(Shift shift : shiftList){
                    String x = String.format("%d-%s-%s", i, i1 < 10 ? "0" + Integer.toString(i1) : Integer.toString(i1), i2 < 10 ? "0" + Integer.toString(i2) : Integer.toString(i2));
                    if(shift.getDateStart().contains(x)){
                        shiftList2.add(new Shift(shift.getDateStart(), shift.getWorkingHours(), shift.getUsername()));
                    }

                }
                adapter = new ShiftAdapter(shiftList2, getContext());
                recyclerView.setAdapter(adapter);
            }
        });

        super.onViewCreated(view, savedInstanceState);
    }
}
