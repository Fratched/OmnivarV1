package com.example.testbot;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "omnivar";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_VARIABLE_QUERY =
                "CREATE TABLE variable (var_name VARCHAR(32) PRIMARY KEY, var_unit VARCHAR(32))";
        db.execSQL(CREATE_VARIABLE_QUERY);

        String CREATE_CONNECTION_QUERY =
                "CREATE TABLE connection (var_from VARCHAR(32), var_to VARCHAR(32))";
        db.execSQL(CREATE_CONNECTION_QUERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS variable");
        db.execSQL("DROP TABLE IF EXISTS connection");
        onCreate(db);
    }
}
