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
import java.util.List;

public class TopGenresActivity extends AppCompatActivity {
    private WrappedDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_genres);

        database = new WrappedDatabase(this);
        String userEmail = getIntent().getStringExtra("email");
        List<SpotifyWrapped> userSpotifyWrapped = database.getSpotifyWrapped(userEmail);
        // Check if there's any SpotifyWrapped data available

        // Get the topTracks from the most recent SpotifyWrapped object
        SpotifyWrapped mostRecentWrapped = userSpotifyWrapped.get(userSpotifyWrapped.size() - 1);
        List<String> topGenres = mostRecentWrapped.getTopGenres();


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
                intent.putExtra("email", userEmail);
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
