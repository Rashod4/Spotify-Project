package com.example.spotifysdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

public class DuoTopSongs extends AppCompatActivity {
    private List<SpotifyWrapped> yourWrappedList; // List to store Spotify wraps
    private List<SpotifyWrapped> friendWrappedList; // List to store Spotify wraps
    private String userEmail;
    private String friendEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duotopsongs);

        userEmail = getIntent().getStringExtra("yourEmail");
        friendEmail = getIntent().getStringExtra("friendEmail");

        // Fetch user's Spotify wraps (replace this with your actual data retrieval logic)
        yourWrappedList = getTopSongs(userEmail);
        friendWrappedList = getTopSongs(friendEmail);
        // Check if the wrapped lists are not null and have data
        if (yourWrappedList != null && yourWrappedList.size() > 0 && friendWrappedList != null && friendWrappedList.size() > 0) {
            // Get the top songs for both users
            SpotifyWrapped yourMostRecentWrapped = yourWrappedList.get(yourWrappedList.size() - 1); // Latest wrapped
            List<String> yourTopSongs = yourMostRecentWrapped.getTopTracks();
            List<String> yourSongImageUrls = yourMostRecentWrapped.getImageUrls();

            SpotifyWrapped friendMostRecentWrapped = friendWrappedList.get(friendWrappedList.size() - 1); // Latest wrapped
            List<String> friendTopSongs = friendMostRecentWrapped.getTopTracks();
            List<String> friendSongImageUrls = friendMostRecentWrapped.getImageUrls();

            // Display top 5 songs for both users
            for (int i = 0; i < 5; i++) {
                // Display user's top song
                int userTextViewId = getResources().getIdentifier("user1topsong" + (i + 1), "id", getPackageName());
                TextView userTextView = findViewById(userTextViewId);
                if (i < yourTopSongs.size()) {
                    userTextView.setText(yourTopSongs.get(i));
                }

                // Display friend's top song
                int friendTextViewId = getResources().getIdentifier("user2topsong" + (i + 1), "id", getPackageName());
                TextView friendTextView = findViewById(friendTextViewId);
                if (i < friendTopSongs.size()) {
                    friendTextView.setText(friendTopSongs.get(i));
                }
            }
        } else {
            // Handle the case where top songs data is not available
            Toast.makeText(this, "Top songs data is not available.", Toast.LENGTH_SHORT).show();
        }
        Button next = findViewById(R.id.duoSongNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DuoTopSongs.this, DuoTopGenres.class);
                intent.putExtra("yourEmail", userEmail);
                intent.putExtra("friendEmail", friendEmail);
                startActivity(intent);
            }
        });
        Button back = findViewById(R.id.duoSongBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private List<SpotifyWrapped> getTopSongs(String email) {
        List<SpotifyWrapped> wraps = new ArrayList<>();

        // Example: Fetch wraps from a database using WrappedDatabase class
        WrappedDatabase database = new WrappedDatabase(this);
        wraps = database.getSpotifyWrapped(email); // Assuming this method returns a list of wraps

        return wraps;
    }
}
