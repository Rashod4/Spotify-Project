package com.example.spotifysdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;


public class TopTracksActivity extends AppCompatActivity {
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_songs);

        // Get the top tracks data from Intent extras
        ArrayList<String> topTracks = getIntent().getStringArrayListExtra("topTracks");
        ArrayList<String> topArtists = getIntent().getStringArrayListExtra("topArtists");
        ArrayList<String> topGenres = getIntent().getStringArrayListExtra("topGenres");

        // Retrieves each song from the tropTracks arraylist and displays them in top_songs.xml
        for (int i = 0; i < 5; i++) {
            int textViewId = getResources().getIdentifier("topSong" + (i + 1), "id", getPackageName());
            TextView textView = findViewById(textViewId);
            textView.setText(topTracks.get(i));
        }


        Button next = findViewById(R.id.topSongsNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopTracksActivity.this, TopArtistsActivity.class);
                intent.putStringArrayListExtra("topArtists", topArtists);
                intent.putStringArrayListExtra("topGenres", topGenres);
                startActivity(intent);
            }
        });
    }
}

