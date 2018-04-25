package es.otherperspectiv.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateOrJoinRestaurantActivity extends AppCompatActivity {

    private Button buttonJoin;
    private Button buttonCreate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_join_restaurant);

        buttonCreate = findViewById(R.id.buttonCreate);
        buttonJoin = findViewById(R.id.buttonJoin);

        buttonJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(CreateOrJoinRestaurantActivity.this, CreateRestaurantActivity.class));

            }
        });
    }
}