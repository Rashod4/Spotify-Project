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

public class TopArtistsActivity extends AppCompatActivity {
    private WrappedDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_artists);

        database = new WrappedDatabase(this);
        String userEmail = getIntent().getStringExtra("email");
        List<SpotifyWrapped> userSpotifyWrapped = database.getSpotifyWrapped(userEmail);
        // Check if there's any SpotifyWrapped data available

        // Get the topTracks from the most recent SpotifyWrapped object
        SpotifyWrapped mostRecentWrapped = userSpotifyWrapped.get(userSpotifyWrapped.size() - 1); //latest wrapped
        List<String> topArtists = mostRecentWrapped.getTopArtists();
        List<String> artistImageUrls = mostRecentWrapped.getArtistImageUrls();


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

        // Load images into each ImageView using Glide
        ArrayList<Integer> imageViewIds = new ArrayList<>();
        imageViewIds.add(R.id.imageView1);
        imageViewIds.add(R.id.imageView2);
        imageViewIds.add(R.id.imageView3);
        imageViewIds.add(R.id.imageView4);
        imageViewIds.add(R.id.imageView5);

        for (int i = 0; i < 5; i++) {
            ImageView imageView = findViewById(imageViewIds.get(i));
            String imageUrl = artistImageUrls.get(i);

            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }

        Button next = findViewById(R.id.topArtistNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopArtistsActivity.this, TopGenresActivity.class);
                intent.putExtra("email", userEmail);
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