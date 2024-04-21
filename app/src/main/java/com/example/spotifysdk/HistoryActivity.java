package com.example.spotifysdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private Spinner historySpinner;
    private List<SpotifyWrapped> wrappedList; // List to store Spotify wraps
    private String userEmail;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wrapped_history);

        userEmail = getIntent().getStringExtra("email");
        // Initialize the spinner
        historySpinner = findViewById(R.id.historySpinner);

        // Fetch user's Spotify wraps (replace this with your logic to fetch from database or API)
        wrappedList = getSpotifyWrapped();

        // Create a list of strings from SpotifyWrapped objects
        List<String> wrappedTitles = new ArrayList<>();
        for (SpotifyWrapped wrapped : wrappedList) {
            // Extract relevant information from each SpotifyWrapped object
            String date = wrapped.getDate(); // Example: Display first track
            wrappedTitles.add(date);
        }

        // Create an ArrayAdapter using the list of strings
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                wrappedTitles
        );

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        historySpinner.setAdapter(adapter);

        // Handle selection events if needed
        historySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDate = (String) parent.getItemAtPosition(position);
                Toast.makeText(HistoryActivity.this, "Selected wrap: " + selectedDate, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Handle empty selection if needed
            }
        });

        Button button = findViewById(R.id.history_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistoryActivity.this, Transition1Activity.class);
                //intent.putExtra("source", "HistoryActivity"); // Indicate source of intent
                intent.putExtra("email", userEmail); // Include other necessary extras
                intent.putExtra("date", selectedDate);
                startActivity(intent);
            }
        });

        Button back = findViewById(R.id.back_btn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    // Method to fetch user's Spotify wraps (replace with your actual data retrieval logic)
    private List<SpotifyWrapped> getSpotifyWrapped() {
        List<SpotifyWrapped> wraps = new ArrayList<>();

        // Example: Fetch wraps from a database using WrappedDatabase class
        WrappedDatabase database = new WrappedDatabase(this);
        wraps = database.getSpotifyWrapped(userEmail); // Assuming this method returns a list of wraps

        return wraps;
    }

    // Helper method to find a SpotifyWrapped object by date
//    private SpotifyWrapped findWrappedByDate(String date) {
//        for (SpotifyWrapped wrapped : wrappedList) {
//            if (wrapped.getDate().equals(date)) {
//                return wrapped;
//            }
//        }
//        return null; // Return null if no match found
//    }
}

