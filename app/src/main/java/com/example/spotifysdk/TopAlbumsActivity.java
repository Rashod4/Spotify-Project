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

public class TopAlbumsActivity extends AppCompatActivity{
    private WrappedDatabase database;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_albums);

        database = new WrappedDatabase(this);
        String userEmail = getIntent().getStringExtra("email");
        List<SpotifyWrapped> userSpotifyWrapped = database.getSpotifyWrapped(userEmail);
        // Check if there's any SpotifyWrapped data available

        // Get the topTracks from the most recent SpotifyWrapped object
        SpotifyWrapped mostRecentWrapped = userSpotifyWrapped.get(userSpotifyWrapped.size() - 1);
        List<String> topAlbums = mostRecentWrapped.getTopAlbums();
        List<String> imageUrls = mostRecentWrapped.getImageUrls();

        if (topAlbums != null) {
            // Update UI to display top tracks
            for (int i = 0; i < 4; i++) {
                int textViewId = getResources().getIdentifier("topAlbum" + (i + 1), "id", getPackageName());
                TextView textView = findViewById(textViewId);
                textView.setText(topAlbums.get(i));
            }
        } else {
            // Handle the case where topArtists is null
            // For example, display a message or log an error
            Log.e("TopGenresActivity", "topGenres ArrayList is null");
        }

//        // Load images into each ImageView using Glide
//        ArrayList<Integer> imageViewIds = new ArrayList<>();
//        imageViewIds.add(R.id.imageView1);
//        imageViewIds.add(R.id.imageView2);
//        imageViewIds.add(R.id.imageView3);
//        imageViewIds.add(R.id.imageView4);
//        imageViewIds.add(R.id.imageView5);
//
//        for (int i = 0; i < 5; i++) {
//            ImageView imageView = findViewById(imageViewIds.get(i));
//            String imageUrl = imageUrls.get(i);
//
//            Glide.with(this)
//                    .load(imageUrl)
//                    .into(imageView);
//        }

        Button back = findViewById(R.id.topAlbumBack);
        Button backToHome = findViewById(R.id.topAlbumNext);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backToHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopAlbumsActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Call this to finish the current activity
            }
        });
    }
}
