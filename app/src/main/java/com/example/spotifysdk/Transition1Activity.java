package com.example.spotifysdk;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Transition1Activity extends AppCompatActivity {
    private String date;
    private String email;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transition_1);
        email = getIntent().getStringExtra("email");
        date = getIntent().getStringExtra("date");
        Button next = findViewById(R.id.trans1_next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Transition1Activity.this, Transition2Activity.class);
                if (date != null && !date.isEmpty()) {
                    intent.putExtra("date", date);
                }
                intent.putExtra("email", email);
                startActivity(intent);
            }
        });

        Button back = findViewById(R.id.trans1_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
