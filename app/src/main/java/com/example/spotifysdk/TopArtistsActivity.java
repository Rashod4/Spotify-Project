package com.example.spotifysdk;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
public class TopArtistsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_artists);

        // Get the top tracks data from Intent extras
        ArrayList<String> topArtists = getIntent().getStringArrayListExtra("topArtists");

        // Check if topArtists is null
        if (topArtists != null) {
            // Update UI to display top tracks
            TextView topArtistsTextView = findViewById(R.id.top_artists_text_view);
            topArtistsTextView.setText(TextUtils.join("\n", topArtists));

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
    }
}