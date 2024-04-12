package com.example.spotifysdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class TopTracksActivity extends AppCompatActivity {
    private int currentTrackIndex = 0;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> previewUrls;
    private WrappedDatabase database;
    private SpotifyWrapped mostRecentWrapped;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_songs);

        database = new WrappedDatabase(this);
        String userEmail = getIntent().getStringExtra("email");

        List<SpotifyWrapped> userSpotifyWrapped = database.getSpotifyWrapped(userEmail);

        // Check if the intent is coming from HistoryActivity
        if (getIntent().hasExtra("date")) {
            // Intent is coming from HistoryActivity
            date = getIntent().getStringExtra("date");
            mostRecentWrapped = findWrappedByDate(userSpotifyWrapped, date);
        } else {
            mostRecentWrapped = userSpotifyWrapped.get(userSpotifyWrapped.size() - 1); //latest wrapped
        }


        // Check if there's any SpotifyWrapped data available

        // Get the topTracks from the most recent SpotifyWrapped object

        List<String> topTracks = mostRecentWrapped.getTopTracks();
        List<String> previewUrls = mostRecentWrapped.getPreviewUrls();
        List<String> imageUrls = mostRecentWrapped.getImageUrls();


        // Retrieves each song from the tropTracks arraylist and displays them in top_songs.xml
        for (int i = 0; i < topTracks.size(); i++) {
            int textViewId = getResources().getIdentifier("topSong" + (i + 1), "id", getPackageName());
            TextView textView = findViewById(textViewId);
            textView.setText(topTracks.get(i));
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
            String imageUrl = imageUrls.get(i);

            Glide.with(this)
                    .load(imageUrl)
                    .into(imageView);
        }

        // Initialize media player
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setOnCompletionListener(mp -> {
            // Play the next track when current track finishes
            currentTrackIndex++;
            if (currentTrackIndex < previewUrls.size()) {
                playTrack(previewUrls.get(currentTrackIndex));
            } else {
                currentTrackIndex = 0; // Reset to start from the first track again
                playTrack(previewUrls.get(currentTrackIndex)); // Loop back to the first track
            }
        });

        // Start playing the first track
        if (!previewUrls.isEmpty()) {
            playTrack(previewUrls.get(currentTrackIndex));
        }

        Button next = findViewById(R.id.topSongsNext);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TopTracksActivity.this, Transition3Activity.class);
                intent.putExtra("email", userEmail);
                if (date != null && !date.isEmpty()) {
                    intent.putExtra("date", date);
                }
                startActivity(intent);
            }
        });

        Button back = findViewById(R.id.topSongsBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private SpotifyWrapped findWrappedByDate(List<SpotifyWrapped> wrappedList, String date) {
        for (SpotifyWrapped wrapped : wrappedList) {
            if (wrapped.getDate().equals(date)) {
                return wrapped;
            }
        }
        return null;
    }

    private void playTrack(String previewUrl) {
        try {
            if (mediaPlayer == null) {
                mediaPlayer = new MediaPlayer();
            } else {
                mediaPlayer.reset(); // Reset the MediaPlayer if it's already initialized
            }

            mediaPlayer.setDataSource(previewUrl);
            mediaPlayer.prepareAsync(); // Prepare asynchronously

            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    // Start playback when the MediaPlayer is prepared
                    mediaPlayer.start();
                }
            });

            mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e("MediaPlayer", "Error during playback: what = " + what + ", extra = " + extra);
                    mediaPlayer.reset(); // Reset the MediaPlayer on error
                    return false;
                }
            });

        } catch (IOException e) {
            Log.e("MediaPlayer", "Error setting data source: " + e.getMessage());
        } catch (IllegalStateException e) {
            Log.e("MediaPlayer", "Illegal state while preparing or starting playback: " + e.getMessage());
            mediaPlayer.reset(); // Reset the MediaPlayer if an IllegalStateException occurs
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Stop and release MediaPlayer resources
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

