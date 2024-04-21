package com.example.spotifysdk;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class LastPageActivity extends AppCompatActivity{
    private WrappedDatabase database;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_page);

        String userEmail = getIntent().getStringExtra("email");

        Button home = findViewById(R.id.last_Page_to_home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopTracksActivity.stopPlayback();
                Intent intent = new Intent(LastPageActivity.this, MainActivity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
            }
        });
    }
}

