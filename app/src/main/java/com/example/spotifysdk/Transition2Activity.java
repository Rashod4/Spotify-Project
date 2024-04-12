package com.example.spotifysdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Transition2Activity extends AppCompatActivity {
    private String date;
    private String email;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_2);
        email = getIntent().getStringExtra("email");
        date = getIntent().getStringExtra("date");
        Button next = findViewById(R.id.trans2_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transition2Activity.this, TopTracksActivity.class);
                if (date != null && !date.isEmpty()) {
                    intent.putExtra("date", date);
                }
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        Button back = findViewById(R.id.trans2_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

