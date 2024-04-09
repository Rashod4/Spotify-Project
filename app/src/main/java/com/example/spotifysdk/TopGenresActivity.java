package com.example.spotifysdk;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
public class TopGenresActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_genres);

        ArrayList<String> topGenres = getIntent().getStringArrayListExtra("topGenres");
        ArrayList<String> topAlbums = getIntent().getStringArrayListExtra("topAlbums");

        if (topGenres != null) {
            // Update UI to display top tracks
            for (int i = 0; i < 5; i++) {
                int textViewId = getResources().getIdentifier("topGenre" + (i + 1), "id", getPackageName());
                TextView textView = findViewById(textViewId);
                textView.setText(topGenres.get(i));
            }
        } else {
            // Handle the case where topArtists is null
            // For example, display a message or log an error
            Log.e("TopGenresActivity", "topGenres ArrayList is null");
        }

        Button next = findViewById(R.id.topGenreNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopGenresActivity.this, TopAlbumsActivity.class);
                intent.putStringArrayListExtra("topAlbums", topAlbums);
                startActivity(intent);
            }
        });

        Button back = findViewById(R.id.topGenreBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
