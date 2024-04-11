package com.example.spotifysdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WrappedDatabase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "WrappedDatabase.db";
    private static final int DATABASE_VERSION = 3; //CHANGE VERSION NUMBER IF YOU ADD COLUMNS TO DATABASE

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
                "previewUrls TEXT," +
                "imageUrls TEXT," +
                "artistImageUrls TEXT," +
                "topAlbums TEXT," +
                "dateEntry DATE," +
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
        values.put("previewUrls", convertListToString(spotifyWrapped.getPreviewUrls()));
        values.put("imageUrls", convertListToString(spotifyWrapped.getImageUrls()));
        values.put("artistImageUrls", convertListToString(spotifyWrapped.getArtistImageUrls()));
        values.put("topAlbums", convertListToString(spotifyWrapped.getTopAlbums()));
        values.put("dateEntry", LocalDateTime.now().toString());

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

    public List<SpotifyWrapped> getSpotifyWrapped(String userEmail) {
        List<SpotifyWrapped> spotifyWrappedList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        // Define the columns to retrieve
        String[] columns = {"email", "topTracks", "topArtists", "topGenres", "previewUrls",
        "imageUrls", "artistImageUrls", "topAlbums", "dateEntry"};

        // Define the selection criteria (WHERE clause)
        String selection = "email = ?";
        String[] selectionArgs = {userEmail};

        // Query the database to retrieve SpotifyWrapped records for the specified user
        Cursor cursor = db.query("SpotifyWrapped", columns, selection, selectionArgs, null, null, null);

        // Iterate through the cursor to retrieve each SpotifyWrapped record
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String email = cursor.getString(0);
                String topTracksStr = cursor.getString(1);
                String topArtistsStr = cursor.getString(2);
                String topGenresStr = cursor.getString(3);
                String previewUrlsStr = cursor.getString(4);
                String imageUrlsStr = cursor.getString(5);
                String artistImageUrlsStr = cursor.getString(6);
                String topAlbumsStr = cursor.getString(7);
                String dateEntryStr = cursor.getString(8);

                // Convert comma-separated strings back to ArrayList<String>
                List<String> topTracks = convertStringToList(topTracksStr);
                List<String> topArtists = convertStringToList(topArtistsStr);
                List<String> topGenres = convertStringToList(topGenresStr);
                List<String> previewUrls = convertStringToList(previewUrlsStr);
                List<String> imageUrls = convertStringToList(imageUrlsStr);
                List<String> artistImageUrls = convertStringToList(artistImageUrlsStr);
                List<String> topAlbums = convertStringToList(topAlbumsStr);
                LocalDateTime dateEntry = LocalDateTime.parse(dateEntryStr);

                // Create a new SpotifyWrapped object and add it to the list
                SpotifyWrapped spotifyWrapped = new SpotifyWrapped(topTracks, topArtists, topGenres, previewUrls,
                        imageUrls, artistImageUrls, topAlbums, dateEntry);
                spotifyWrappedList.add(spotifyWrapped);
            } while (cursor.moveToNext());
        }

        // Close the cursor and database connection
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return spotifyWrappedList;
    }
    // Helper method to convert a comma-separated string to a list of strings
    private List<String> convertStringToList(String str) {
        return Arrays.asList(str.split(","));
    }

}
