package com.example.spotifysdk;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LastPage2Activity extends AppCompatActivity{
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.last_page2);

        String userEmail = getIntent().getStringExtra("email");

        Button home = findViewById(R.id.last_Page_to_home2);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TopTracksActivity.stopPlayback();
                Intent intent = new Intent(LastPage2Activity.this, MainActivity.class);
                intent.putExtra("email", userEmail);
                startActivity(intent);
            }
        });
    }
}
