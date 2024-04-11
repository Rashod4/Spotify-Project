package com.example.spotifysdk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity{
    private WrappedDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped_history);

        database = new WrappedDatabase(this);
        String userEmail = getIntent().getStringExtra("email");
        List<SpotifyWrapped> userSpotifyWrapped = database.getSpotifyWrapped(userEmail);

        Spinner spinner = findViewById(R.id.historySpinner);
        List<LocalDateTime> dateTimeList = new ArrayList<>();
        for (int i = 0; i < userSpotifyWrapped.size(); i++) {
            dateTimeList.add(userSpotifyWrapped.get(i).getDateTime());
        }
        List<String> strList = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm:ss");
        for (LocalDateTime dateTime : dateTimeList) {
            strList.add(dateTime.format(formatter));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, strList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        Button display = findViewById(R.id.history_button);
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
