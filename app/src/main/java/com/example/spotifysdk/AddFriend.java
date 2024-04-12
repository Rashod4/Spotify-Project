package com.example.spotifysdk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class AddFriend extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        EditText email_text = (EditText) findViewById(R.id.yourEmail);
        EditText friend_email = (EditText) findViewById(R.id.friendEmail);
        Button send_btn = (Button) findViewById(R.id.send_button);
        send_btn.setOnClickListener((v) -> {
            validate(email_text.getText().toString(), friend_email.getText().toString());
        });
    }
    public void validate(String youremail, String friendEmail) {
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (youremail == null || friendEmail == null) {
            Toast.makeText(this, "Sign up input's must be non-null", Toast.LENGTH_SHORT).show();
            return;
        } else if (!youremail.matches(regexEmail) || !friendEmail.matches(regexEmail)) {
            Toast.makeText(this, "Invalid email(s)!", Toast.LENGTH_SHORT).show();
            return;
        }
        WrappedDatabase wrappedDatabase = new WrappedDatabase(getApplicationContext());
        List<SpotifyWrapped> yourWrapped = wrappedDatabase.getSpotifyWrapped(youremail);
        if (yourWrapped == null || yourWrapped.size() == 0) {
            Toast.makeText(this, "You do not have any existing wraps", Toast.LENGTH_SHORT).show();
            return;
        }
        List<SpotifyWrapped> friendWrapped = wrappedDatabase.getSpotifyWrapped(friendEmail);
        if (friendWrapped == null || friendWrapped.size() == 0) {
            Toast.makeText(this, "Your friend does not have any existing wraps", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, DuoTopArtists.class);
        intent.putExtra("yourEmail", youremail);
        intent.putExtra("friendEmail", youremail);
        startActivity(intent);
        finish(); // Call this to finish the current activity
    }
}
