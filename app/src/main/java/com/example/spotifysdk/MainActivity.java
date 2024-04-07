package com.example.spotifysdk;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "b838b340b00049e299cb9e9bb3a6b005";
    public static final String REDIRECT_URI = "com.example.spotifysdk://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    private TextView tokenTextView, codeTextView, profileTextView;
    private ArrayList<String> trackNames = new ArrayList<>();
    private ArrayList<String> artistsNames = new ArrayList<>();
    private List<String> genreNames;
    private WrappedDatabase wrappedDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the views
        tokenTextView = (TextView) findViewById(R.id.token_text_view);
        codeTextView = (TextView) findViewById(R.id.code_text_view);
        profileTextView = (TextView) findViewById(R.id.response_text_view);


        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);
        Button codeBtn = (Button) findViewById(R.id.code_btn);
        //Button profileBtn = (Button) findViewById(R.id.profile_btn);
        Button createWrappedBtn = findViewById(R.id.create_wrapped_btn);

        // Set the click listeners for the buttons

        tokenBtn.setOnClickListener((v) -> {
            getToken();
        });

        codeBtn.setOnClickListener((v) -> {
            getCode();
        });

//        profileBtn.setOnClickListener((v) -> {
//            onGetUserProfileClicked();
//        });

        createWrappedBtn.setOnClickListener((v) -> {
            generateSpotifyWrapped();
        });

    }

    /**
     * Get token from Spotify
     * This method will open the Spotify login activity and get the token
     * What is token?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(MainActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    /**
     * Get code from Spotify
     * This method will open the Spotify login activity and get the code
     * What is code?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(MainActivity.this, AUTH_CODE_REQUEST_CODE, request);
    }


    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
            setTextAsync(mAccessToken, tokenTextView);

        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
            mAccessCode = response.getCode();
            setTextAsync(mAccessCode, codeTextView);
        }
    }


    private void generateSpotifyWrapped() {
        if (mAccessToken == null) {
            Toast.makeText(this, "Please authenticate with Spotify first", Toast.LENGTH_SHORT).show();
            return;
        }

        // Make a combined request to fetch top tracks and top artists
        Request topTracksRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?limit=5")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        Request topArtistsRequest = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/artists?limit=5")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall(); // Cancel any existing call
        mCall = mOkHttpClient.newCall(topTracksRequest);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HTTP", "Failed to fetch top tracks: " + e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch top tracks", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray items = jsonObject.getJSONArray("items");

                    // Populate trackNames with track names
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String trackName = item.getString("name");
                        trackNames.add(trackName);
                    }

                    // After fetching top tracks, proceed to fetch top artists
                    fetchTopArtists(topArtistsRequest);

                } catch (JSONException e) {
                    Log.e("JSON", "Failed to parse top tracks data: " + e);
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to parse top tracks data", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void fetchTopArtists(Request request) {
        cancelCall(); // Cancel any existing call
        mCall = mOkHttpClient.newCall(request);

        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("HTTP", "Failed to fetch top artists: " + e);
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to fetch top artists", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray items = jsonObject.getJSONArray("items");

                    // Populate artistsNames with artist names
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject item = items.getJSONObject(i);
                        String artistName = item.getString("name");
                        artistsNames.add(artistName);
                    }

                    //ALGORITHM TO FIND TOP GENRES BASED ON ARTIST GENRES (THERE IS NO GENRE ENDPOINT)
                    // Map to count genre occurrences
                    Map<String, Integer> genreCountMap = new HashMap<>();

                    // Iterate over each artist
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject artistObject = items.getJSONObject(i);
                        JSONArray genresArray = artistObject.getJSONArray("genres");

                        // Iterate over genres of the artist
                        for (int j = 0; j < genresArray.length(); j++) {
                            String genre = genresArray.getString(j);
                            // Update genre count map
                            if (genreCountMap.containsKey(genre)) {
                                int count = genreCountMap.get(genre);
                                genreCountMap.put(genre, count + 1);
                            } else {
                                genreCountMap.put(genre, 1);
                            }
                        }
                    }

                    genreNames = getTopGenres(genreCountMap, 5);

                    // After fetching both top tracks and top artists, start the next activity
                    startTopTracksActivity();

                } catch (JSONException e) {
                    Log.e("JSON", "Failed to parse top artists data: " + e);
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Failed to parse top artists data", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void startTopTracksActivity() {
        //making new database entry
        wrappedDatabase = new WrappedDatabase(this);
        SpotifyWrapped sw = new SpotifyWrapped(trackNames, artistsNames);
        String email = "r@gmail.com"; //CHANGE THIS TO GET CURRENT USER EMAIL!!!!!!!!!!!!!!!!!!!!!!
        wrappedDatabase.insertSpotifyWrapped(sw, email);

        //starting spotify wrapped
        Intent intent = new Intent(MainActivity.this, TopTracksActivity.class);
        intent.putStringArrayListExtra("topTracks", trackNames);
        intent.putStringArrayListExtra("topArtists", artistsNames);
        intent.putStringArrayListExtra("topGenres", (ArrayList<String>) genreNames);
        startActivity(intent);
    }

    private List<String> getTopGenres(Map<String, Integer> genreCountMap, int numGenres) {
        List<String> topGenres = new ArrayList<>();

        // Sort genres by count in descending order
        genreCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(numGenres)
                .forEachOrdered(entry -> topGenres.add(entry.getKey()));

        return topGenres;
    }

    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }



    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[] { "user-top-read" }) // <--- Change the scope of your requested token here
                .setCampaign("your-campaign-token")
                .build();
    }

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }
}