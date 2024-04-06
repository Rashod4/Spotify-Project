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

        // Retrieves each song from the tropTracks arraylist and displays them in top_songs.xml
        for (int i = 0; i < 5; i++) {
            int textViewId = getResources().getIdentifier("topSong" + (i + 1), "id", getPackageName());
            TextView textView = findViewById(textViewId);
            textView.setText(topTracks.get(i));
        }



    }
}

