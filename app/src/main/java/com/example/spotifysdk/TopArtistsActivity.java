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
public class TopArtistsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_artists);

        // Get the top tracks data from Intent extras
        ArrayList<String> topArtists = getIntent().getStringArrayListExtra("topArtists");
        ArrayList<String> topGenres = getIntent().getStringArrayListExtra("topGenres");
        ArrayList<String> topAlbums = getIntent().getStringArrayListExtra("topAlbums");

        // Check if topArtists is null
        if (topArtists != null) {
            // Update UI to display top tracks
            for (int i = 0; i < 5; i++) {
                int textViewId = getResources().getIdentifier("topArtist" + (i + 1), "id", getPackageName());
                TextView textView = findViewById(textViewId);
                textView.setText(topArtists.get(i));
            }
        } else {
            // Handle the case where topArtists is null
            // For example, display a message or log an error
            Log.e("TopArtistsActivity", "topArtists ArrayList is null");
        }

        Button next = findViewById(R.id.topArtistNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopArtistsActivity.this, TopGenresActivity.class);
                intent.putStringArrayListExtra("topGenres", topGenres);
                intent.putStringArrayListExtra("topAlbums", topAlbums);
                startActivity(intent);
            }
        });

        Button back = findViewById(R.id.topArtistBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}