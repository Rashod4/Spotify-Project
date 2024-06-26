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

public class DuoTopGenres extends AppCompatActivity {
    private List<SpotifyWrapped> yourWrappedList; // List to store Spotify wraps
    private List<SpotifyWrapped> friendWrappedList; // List to store Spotify wraps
    private String userEmail;
    private String friendEmail;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.duotopgenres);

        userEmail = getIntent().getStringExtra("yourEmail");
        friendEmail = getIntent().getStringExtra("friendEmail");

        // Fetch user's Spotify wraps (replace this with your actual data retrieval logic)
        yourWrappedList = getTopGenres(userEmail);
        friendWrappedList = getTopGenres(friendEmail);

        // Check if the wrapped lists are not null and have data
        if (yourWrappedList != null && yourWrappedList.size() > 0 && friendWrappedList != null && friendWrappedList.size() > 0) {
            // Get the top genres for both users
            SpotifyWrapped yourMostRecentWrapped = yourWrappedList.get(yourWrappedList.size() - 1); // Latest wrapped
            List<String> yourTopGenres = yourMostRecentWrapped.getTopGenres();

            SpotifyWrapped friendMostRecentWrapped = friendWrappedList.get(friendWrappedList.size() - 1); // Latest wrapped
            List<String> friendTopGenres = friendMostRecentWrapped.getTopGenres();

            // Display top 5 genres for both users
            for (int i = 0; i < 5; i++) {
                // Display user's top genre
                int userTextViewId = getResources().getIdentifier("user1topgenre" + (i + 1), "id", getPackageName());
                TextView userTextView = findViewById(userTextViewId);
                if (i < yourTopGenres.size()) {
                    userTextView.setText(yourTopGenres.get(i));
                }

                // Display friend's top genre
                int friendTextViewId = getResources().getIdentifier("user2topgenre" + (i + 1), "id", getPackageName());
                TextView friendTextView = findViewById(friendTextViewId);
                if (i < friendTopGenres.size()) {
                    friendTextView.setText(friendTopGenres.get(i));
                }
            }
        } else {
            // Handle the case where top genres data is not available
            Toast.makeText(this, "Top genres data is not available.", Toast.LENGTH_SHORT).show();
        }
        Button back = findViewById(R.id.duoGenreBack);
        Button next = findViewById(R.id.duoGenreNext);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DuoTopGenres.this, LastPage2Activity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
            }
        });

    }
    private List<SpotifyWrapped> getTopGenres(String email) {
        List<SpotifyWrapped> wraps = new ArrayList<>();

        WrappedDatabase database = new WrappedDatabase(this);
        wraps = database.getSpotifyWrapped(email); // Assuming this method returns a list of wraps

        return wraps;
    }
}
