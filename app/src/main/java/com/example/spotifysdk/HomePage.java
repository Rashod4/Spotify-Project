package com.example.spotifysdk;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    //OnClick profile_btn: new tab w temporary homepage for our App
    //displays user profile info (profile textview)
    protected void OnCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.homepage);
    }
}
