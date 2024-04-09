package com.example.spotifysdk;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditAccount extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accountsettings);
        EditText email_change = (EditText) findViewById(R.id.email_change);
        EditText password_change = (EditText) findViewById(R.id.password_change);
        EditText full_name = (EditText) findViewById(R.id.full_Name);
        Button edit_button = (Button) findViewById(R.id.edit_login_info);
        Button delete_button = (Button) findViewById(R.id.delete);
        edit_button.setOnClickListener((v) -> {
            saveChanges(full_name.getText().toString(), email_change.getText().toString(), password_change.getText().toString());
        });
        delete_button.setOnClickListener((v) -> {
            deleteChanges(full_name.getText().toString());
        });
    }
    public void saveChanges(String name, String email, String pwd) {
        String regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        String[] parts = name.split(" ");
        if (email == null || pwd == null || name == null) {
            Toast.makeText(this, "Edit input's must be non-null", Toast.LENGTH_SHORT).show();
            return;
        } else if (!email.matches(regexEmail)) {
            Toast.makeText(this, "Invalid email!", Toast.LENGTH_SHORT).show();
            return;
        } else if (parts.length <= 1) {
            Toast.makeText(this, "Must enter first and last name!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            LoginDatabase database = LoginDatabase.getInstance(getApplicationContext());
            ContentValues values = new ContentValues();
            values.put("email", email); // newEmail is the email to update
            values.put("password", pwd); // newPassword is the password to update
            String selection = "firstName = ? AND lastName = ?";
            String first = parts[0];
            String last = parts[1];
            String[] selectionArgs = {first, last};
            // this cursor part queries that database to see if the name exists
            Cursor cursor = database.getReadableDatabase().query(
                    "LoginData",   // The table to query
                    new String[]{"id"}, // The columns to return (just need one to check existence)
                    selection,      // The columns for the WHERE clause
                    selectionArgs,  // The values for the WHERE clause
                    null,           // Don't group the rows
                    null,           // Don't filter by row groups
                    null            // The sort order
            );
            if (cursor != null && cursor.moveToFirst()) {
                // selection criteria
                int count = database.getWritableDatabase().update(
                        "LoginData",   // Table name
                        values,         // New values
                        selection,      // Selection criteria
                        selectionArgs   // Selection arguments
                );
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish(); // Call this to finish the current activity
            } else {
                Toast.makeText(this, "Name does not exist in database", Toast.LENGTH_SHORT).show();
                return;
            }

        }
    }
    public void deleteChanges(String name) {
        if (name == null) {
            Toast.makeText(this, "Verify name before deleting account!", Toast.LENGTH_SHORT).show();
            return;
        }
        String[] parts = name.split(" ");
        if (parts.length <=1) {
            Toast.makeText(this, "Enter first and last name to delete!", Toast.LENGTH_SHORT).show();
            return;
        }
        LoginDatabase database = LoginDatabase.getInstance(getApplicationContext());
        String selection = "firstName = ? AND lastName = ?";
        String[] selectionArgs = { parts[0], parts[1] }; // firstName and lastName should be the values you're looking for
        int deletedRows = database.getWritableDatabase().delete(
                "LoginData",  // The table name
                selection,     // The selection criteria (WHERE clause)
                selectionArgs  // The arguments for the selection criteria
        );
        if (deletedRows > 0) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish(); // Call this to finish the current activity
        } else {
            Toast.makeText(this, "Verify name does not exist", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}
