package com.example.spotifysdk;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class LoginDatabase extends SQLiteOpenHelper {
    private static LoginDatabase sInstance;
    private static final String DATABASE_NAME = "LoginData.db";
    private static final int DATABASE_VERSION = 2;

    private LoginDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized LoginDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new LoginDatabase(context.getApplicationContext());
        }
        return sInstance;
    }

    //this is called the first time a database is created
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE LoginData (id INTEGER PRIMARY KEY AUTOINCREMENT, firstName TEXT, lastName TEXT,email TEXT, password TEXT)";
        db.execSQL(createTable);
    }

    // this is called if the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS LoginData");
        onCreate(db);
    }
    // Method to insert a new user into the database
    public void insertUser(String firstName, String lastName, String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("firstName", firstName);
        values.put("lastName", lastName);
        values.put("email", email);
        values.put("password", password);
        db.insert("LoginData", null, values);
        db.close();
    }

    // Method to check if a user exists in the database based on email and password
    public boolean checkUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT id FROM LoginData WHERE email = ? AND password = ?";
        Cursor cursor = db.rawQuery(query, new String[] {email, password});
        boolean exists = cursor.moveToFirst();
        cursor.close();
        db.close();
        return exists;
    }
}
