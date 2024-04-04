package com.example.spotifysdk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;


public class TopTracksActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_songs);

        // Get the top tracks data from Intent extras
        ArrayList<String> topTracks = getIntent().getStringArrayListExtra("topTracks");

        // Update UI to display top tracks
        TextView topTracksTextView = findViewById(R.id.top_songs_text_view);
        topTracksTextView.setText(TextUtils.join("\n", topTracks));
    }
}

