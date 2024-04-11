package com.example.spotifysdk;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddFriend extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_friend);
        EditText email_text = (EditText) findViewById(R.id.enterEmail);
        Button send_btn = (Button) findViewById(R.id.send_button);
        send_btn.setOnClickListener((v) -> {
            //validate(first_name.getText().toString(), last_name.getText().toString(),email_text.getText().toString(), password_text.getText().toString());
        });
    }
    public void validate(String firstName, String lastName, String email, String password) {
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        if (firstName == null || lastName == null || email == null || password == null) {
            Toast.makeText(this, "Sign up input's must be non-null", Toast.LENGTH_SHORT).show();
            return;
        } else if (!email.matches(regexEmail)) {
            Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            LoginDatabase database = LoginDatabase.getInstance(getApplicationContext());
            database.insertUser(firstName, lastName, email, password);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Call this to finish the current activity
        }
    }
}
