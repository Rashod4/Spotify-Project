package com.example.spotifysdk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class DuoTopArtists extends AppCompatActivity {

    private Spinner historySpinner;
    private List<SpotifyWrapped> yourWrappedList; // List to store Spotify wraps
    private List<SpotifyWrapped> friendWrappedList; // List to store Spotify wraps
    private String userEmail;
    private String friendEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duotopartists);

        userEmail = getIntent().getStringExtra("yourEmail");
        friendEmail = getIntent().getStringExtra("friendEmail");
        // Fetch user's Spotify wraps (replace this with your logic to fetch from database or API)
        yourWrappedList = getTopArtists(userEmail);
        friendWrappedList = getTopArtists(friendEmail);
        // your data
        SpotifyWrapped yourMostRecentWrapped = yourWrappedList.get(yourWrappedList.size() - 1); //latest wrapped
        List<String> yourTopArtists = yourMostRecentWrapped.getTopArtists();
        List<String> yourArtistImageUrls = yourMostRecentWrapped.getArtistImageUrls();
        // friend data
        SpotifyWrapped friendMostRecentWrapped = friendWrappedList.get(friendWrappedList.size() - 1); //latest wrapped
        List<String> friendTopArtists = friendMostRecentWrapped.getTopArtists();
        List<String> friendArtistImageUrls = friendMostRecentWrapped.getArtistImageUrls();

        // Check if topArtists is null
        if (yourTopArtists != null && yourTopArtists.size() > 0) {
            System.out.println("LENGTH: " + yourTopArtists.size());
            // Update UI to display top tracks
            for (int i = 0; i < 5; i++) {
                int textViewId = getResources().getIdentifier("user1TA" + (i + 1), "id", getPackageName());
                TextView textView = findViewById(textViewId);
                textView.setText(yourTopArtists.get(i));
            }
        } else {
            // Handle the case where topArtists is null
            // For example, display a message or log an error
            Log.e("DuoTopArtistsActivity", "topArtists ArrayList is null");
        }
        if (friendTopArtists != null && friendTopArtists.size() > 0) {
            // Update UI to display top tracks
            for (int i = 0; i < 5; i++) {
                int textViewId = getResources().getIdentifier("user2TA" + (i + 1), "id", getPackageName());
                TextView textView = findViewById(textViewId);
                textView.setText(friendTopArtists.get(i));
            }
        } else {
            // Handle the case where topArtists is null
            // For example, display a message or log an error
            Log.e("DuoTopArtistsActivity", "topArtists ArrayList is null");
        }
    }

    // Method to fetch user's Spotify wraps (replace with your actual data retrieval logic)
    private List<SpotifyWrapped> getTopArtists(String email) {
        List<SpotifyWrapped> wraps = new ArrayList<>();

        // Example: Fetch wraps from a database using WrappedDatabase class
        WrappedDatabase database = new WrappedDatabase(this);
        wraps = database.getSpotifyWrapped(email); // Assuming this method returns a list of wraps

        return wraps;
    }
}

