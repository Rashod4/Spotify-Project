package com.example.spotifysdk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
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
        ArrayList<String> imageUrls = getIntent().getStringArrayListExtra("imageUrls");
        ArrayList<String> topAlbums = getIntent().getStringArrayListExtra("topAlbums");

        // Retrieves each song from the tropTracks arraylist and displays them in top_songs.xml
        for (int i = 0; i < 5; i++) {
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
                Intent intent = new Intent(TopTracksActivity.this, TopArtistsActivity.class);
                intent.putStringArrayListExtra("topArtists", topArtists);
                intent.putStringArrayListExtra("topGenres", topGenres);
                intent.putStringArrayListExtra("topAlbums", topAlbums);
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



//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
//            mediaPlayer.pause();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mediaPlayer != null) {
//            mediaPlayer.release();
//            mediaPlayer = null;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Resume playback when the activity is resumed
//        if (mediaPlayer != null && !mediaPlayer.isPlaying()) {
//            mediaPlayer.start();
//        }
//    }
}

