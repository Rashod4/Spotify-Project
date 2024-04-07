package com.example.spotifysdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.text.TextUtils;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;


public class TopTracksActivity extends AppCompatActivity {
    private int currentTrackIndex = 0;
    private MediaPlayer mediaPlayer;
    private ArrayList<String> previewUrls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_songs);

        // Get the top tracks data from Intent extras
        ArrayList<String> topTracks = getIntent().getStringArrayListExtra("topTracks");
        previewUrls = getIntent().getStringArrayListExtra("previewUrls");
        ArrayList<String> topArtists = getIntent().getStringArrayListExtra("topArtists");
        ArrayList<String> topGenres = getIntent().getStringArrayListExtra("topGenres");

        // Retrieves each song from the tropTracks arraylist and displays them in top_songs.xml
        for (int i = 0; i < 5; i++) {
            int textViewId = getResources().getIdentifier("topSong" + (i + 1), "id", getPackageName());
            TextView textView = findViewById(textViewId);
            textView.setText(topTracks.get(i));
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
                Intent intent = new Intent(TopTracksActivity.this, TopArtistsActivity.class);
                intent.putStringArrayListExtra("topArtists", topArtists);
                intent.putStringArrayListExtra("topGenres", topGenres);
                startActivity(intent);
            }
        });
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
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Resume playback when the activity is resumed
        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
}

