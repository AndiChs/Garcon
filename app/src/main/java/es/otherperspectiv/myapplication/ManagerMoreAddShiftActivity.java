package es.otherperspectiv.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ManagerMoreAddShiftActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private TextView tvDate;
    private TextView tvHour;
    private EditText etUsername;
    private EditText etHours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_more_add_shift);

        findViewById(R.id.btnAddShift).setOnClickListener(this);
        findViewById(R.id.btnSelectDate).setOnClickListener(this);
        findViewById(R.id.btnSelectHour).setOnClickListener(this);

        tvDate = findViewById(R.id.tvDate);
        tvHour = findViewById(R.id.tvHour);
        etUsername = findViewById(R.id.etUsername);
        etHours = findViewById(R.id.etHours);

    }

    @Override
    public void onClick(View view) {
        Calendar now = Calendar.getInstance();
        switch (view.getId()) {
            case R.id.btnAddShift:
                if(tvHour.getText().toString().isEmpty()){
                    Toast.makeText(this, "You did not select the hour.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(tvDate.getText().toString().isEmpty()){
                    Toast.makeText(this, "You did not select the date.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(etHours.getText().toString().isEmpty()){
                    Toast.makeText(this, "You did not fill in the working hours.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(etUsername.getText().toString().isEmpty()){
                    Toast.makeText(this, "You did not fill in the username.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(!isInteger(etHours.getText().toString())){
                    Toast.makeText(this, "You introduce an invalid number of working hours.", Toast.LENGTH_SHORT).show();
                    return;
                }

                int workingHours = Integer.parseInt(etHours.getText().toString());

                if(workingHours > 16 || workingHours < 1){
                    Toast.makeText(this, "You introduce an invalid number of working hours.", Toast.LENGTH_SHORT).show();
                    return;
                }



                StringRequest stringRequest = new StringRequest(
                        Request.Method.POST,
                        Constants.URL_ADD_SHIFT,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    Toast.makeText(ManagerMoreAddShiftActivity.this, obj.getString("message"), Toast.LENGTH_LONG).show();
                                    if(!obj.getBoolean("error")){
                                        startActivity(new Intent(getApplicationContext(), MainManagerActivity.class));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                ){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("username", etUsername.getText().toString());
                        params.put("workingHours", etHours.getText().toString());
                        params.put("restaurantId", Integer.toString(User.getInstance(getApplicationContext()).getUserRestaurantId()));
                        params.put("dateStart", tvDate.getText().toString() + " " + tvHour.getText().toString());
                        params.put("token", FirebaseInstanceId.getInstance().getToken());
                        return params;
                    }
                };
                RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
                break;
            case R.id.btnSelectDate:

                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        ManagerMoreAddShiftActivity.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
                break;
            case R.id.btnSelectHour:
                now = Calendar.getInstance();
                TimePickerDialog tpd = TimePickerDialog.newInstance(
                        ManagerMoreAddShiftActivity.this,
                        now.get(Calendar.HOUR),
                        now.get(Calendar.MINUTE),
                        now.get(Calendar.SECOND),
                        true
                );
                tpd.show(getFragmentManager(), "Timepickerdialog");

                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = String.format("%d-%s-%s", year, (monthOfYear+1) < 10  ? "0" + Integer.toString(monthOfYear+1) : Integer.toString(monthOfYear+1) , dayOfMonth < 10 ? "0" + Integer.toString(dayOfMonth) : Integer.toString(dayOfMonth));
        tvDate.setText(date);
        //dateTextView.setText(date);
    }


    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String date = String.format("%s:%s:%s", (hourOfDay) < 10  ? "0" + Integer.toString(hourOfDay) : Integer.toString(hourOfDay), (minute) < 10  ? "0" + Integer.toString(minute) : Integer.toString(minute), (second) < 10  ? "0" + Integer.toString(second) : Integer.toString(second));
        tvHour.setText(date);
    }

    public static boolean isInteger(String s) {
        return isInteger(s,10);
    }

    public static boolean isInteger(String s, int radix) {
        if(s.isEmpty()) return false;
        for(int i = 0; i < s.length(); i++) {
            if(i == 0 && s.charAt(i) == '-') {
                if(s.length() == 1) return false;
                else continue;
            }
            if(Character.digit(s.charAt(i),radix) < 0) return false;
        }
        return true;
    }

}
