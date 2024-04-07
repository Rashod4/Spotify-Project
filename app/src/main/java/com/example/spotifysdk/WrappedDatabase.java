package com.example.spotifysdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.List;

public class WrappedDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WrappedDatabase.db";
    private static final int DATABASE_VERSION = 2; //CHANGE VERSION NUMBER IF YOU ADD COLUMNS TO DATABASE

    public WrappedDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create new table for SpotifyWrapped data with a foreign key constraint on email
        String createTable = "CREATE TABLE SpotifyWrapped (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT," +
                "topTracks TEXT," +
                "topArtists TEXT," +
                "topGenres TEXT," +
                "FOREIGN KEY(email) REFERENCES LoginData(email)" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS SpotifyWrapped");
        onCreate(db);
    }

    // Method to insert a new SpotifyWrapped object into the database
    public void insertSpotifyWrapped(SpotifyWrapped spotifyWrapped, String userEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("email", userEmail);
        values.put("topTracks", convertListToString(spotifyWrapped.getTopTracks()));
        values.put("topArtists", convertListToString(spotifyWrapped.getTopArtists()));
        values.put("topGenres", convertListToString(spotifyWrapped.getTopGenres()));

        // Insert the values into the SpotifyWrapped table
        db.insert("SpotifyWrapped", null, values);
        db.close();
    }

    // Helper method to convert a list of strings to a single string separated by commas
    private String convertListToString(List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item).append(",");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1); // Remove the last comma
        }
        return sb.toString();
    }
}
