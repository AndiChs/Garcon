package es.otherperspectiv.myapplication;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;
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
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class WaiterHomeFragment extends Fragment{

    private CalendarView calendarView;
    private ArrayList<Shift> shifts = new ArrayList<>();
    private TextView tvInfoCalendar;

    public WaiterHomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_waiter_home, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        calendarView = (CalendarView) view.findViewById(R.id.viewCalendar);
        tvInfoCalendar = (TextView) view.findViewById(R.id.tvInfoCalendar);

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                Constants.URL_WAITER_SHIFTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            if(!obj.getBoolean("error")){
                                JSONArray jsonArray = obj.getJSONArray("message");
                                for(int i = 0; i < jsonArray.length(); i++){
                                    JSONObject o = jsonArray.getJSONObject(i);
                                    shifts.add(new Shift(o.getString("date_start"), o.getInt("working_hours")));
                                    System.out.println(o.get("date_start"));
                                }
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
                params.put("userId", Integer.toString(SharedPrefManager.getInstance(getContext()).getUserId()));
                return params;
            }
        };

        RequestHandler.getInstance(getContext()).addToRequestQueue(stringRequest);



        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {
                i1++;
                tvInfoCalendar.setText("You are not working this day.");
                for(Shift shift : shifts){
                    String x = String.format("%d-%s-%s", i, i1 < 10 ? "0" + Integer.toString(i1) : Integer.toString(i1), i2 < 10 ? "0" + Integer.toString(i2) : Integer.toString(i2));
                    if(shift.getDateStart().contains(x)){
                        tvInfoCalendar.setText(shift.getDateStart() + "- Working Hours " + shift.getWorkingHours());
                        break;
                    }

                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }
}
