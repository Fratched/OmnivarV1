package com.example.testbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testbot.DatabaseHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class DataAccessObject {
    private DatabaseHelper helper;

    public DataAccessObject(Context context) {
        helper = new DatabaseHelper(context);
    }

    public void newVariable(VariableHeader variableHeader) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("var_name", variableHeader.getName());
        values.put("var_unit", variableHeader.getUnit().toString());
        db.insert("variable", null, values);
        db.close();
    }

    public void newConnection(ConnectionHeader connectionHeader) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("var_from", connectionHeader.getFrom());
        values.put("var_to", connectionHeader.getTo());
        db.insert("connection", null, values);
        db.close();
    }

    public ArrayList<VariableHeader> getAllVariables() {
        ArrayList<VariableHeader> result = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("variable",
                new String[]{"var_name", "var_unit"},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                String unitString = cursor.getString(1);
                VariableType unit = VariableType.valueOf(unitString);
                result.add(new VariableHeader(name, unit));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }

    public ArrayList<ConnectionHeader> getAllConnections() {
        ArrayList<ConnectionHeader> result = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query("connection",
                new String[]{"var_from", "var_to"},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String name_from = cursor.getString(0);
                String name_to = cursor.getString(1);
                result.add(new ConnectionHeader(name_from, name_to));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return result;
    }
}