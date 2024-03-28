package com.example.spotifysdk;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "b838b340b00049e299cb9e9bb3a6b005";
    public static final String REDIRECT_URI = "com.example.spotifysdk://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken, mAccessCode;
    private Call mCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        EditText email_text = (EditText) findViewById(R.id.email_login);
        EditText password_text = (EditText) findViewById(R.id.password_login);
        Button login_button = (Button) findViewById(R.id.login_button);
        LoginDatabase database = new LoginDatabase(this);
        database.insertUser("liudidi.1251@gmail.com", "password");
        login_button.setOnClickListener((v) -> {
            login(email_text.getText().toString(), password_text.getText().toString());
        });
    }

    public void login(String email, String password) {
        if (email == null || password == null) {
            Toast.makeText(this, "Login input invalid", Toast.LENGTH_SHORT).show();
            return;
        } else {
            LoginDatabase database = new LoginDatabase(this);
            if (database.checkUser(email, password)) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish(); // Call this to finish the current activity
            } else {
                Toast.makeText(this, "Either username or pwd is incorrect", Toast.LENGTH_SHORT).show();
                return;
            }
        }
    }

    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

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