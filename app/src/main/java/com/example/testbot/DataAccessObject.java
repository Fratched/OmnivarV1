package com.example.testbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testbot.model.ConnectionHeader;
import com.example.testbot.model.VariableHeader;
import com.example.testbot.model.VariableType;

import java.util.ArrayList;

public class DataAccessObject {
    private static DataAccessObject instance;
    private final DatabaseHelper helper;

    public final ArrayList<VariableHeader> variables = new ArrayList<>();
    public final ArrayList<ConnectionHeader> connections = new ArrayList<>();
    private boolean variablesLoaded = false;
    private boolean connectionsLoaded = false;

    private DataAccessObject(Context context) {
        helper = new DatabaseHelper(context.getApplicationContext());
    }

    public static DataAccessObject getInstance(Context context) {
        if (instance == null) {
            synchronized (DataAccessObject.class) {
                if (instance == null) {
                    instance = new DataAccessObject(context);
                    instance.getAllVariables();
                    instance.getAllConnections();
                }
            }
        }
        return instance;
    }

    public void newVariable(VariableHeader variableHeader) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("var_name", variableHeader.getName());
        values.put("var_unit", variableHeader.getUnit().toString());
        db.insert("variable", null, values);
        db.close();

        variables.add(variableHeader);
    }

    public void newConnection(ConnectionHeader connectionHeader) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("var_from", connectionHeader.getFrom());
        values.put("var_to", connectionHeader.getTo());
        db.insert("connection", null, values);
        db.close();

        connections.add(connectionHeader);
    }

    private ArrayList<VariableHeader> getAllVariables() {
        if (!variablesLoaded) {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("variable",
                    new String[]{"var_name", "var_unit"},
                    null, null, null, null, null);

            variables.clear();
            if (cursor.moveToFirst()) {
                do {
                    String name = cursor.getString(0);
                    String unitString = cursor.getString(1);
                    VariableType unit = VariableType.valueOf(unitString);
                    variables.add(new VariableHeader(name, unit));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            variablesLoaded = true;
        }
        return variables;
    }

    private ArrayList<ConnectionHeader> getAllConnections() {
        if (!connectionsLoaded) {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query("connection",
                    new String[]{"var_from", "var_to"},
                    null, null, null, null, null);

            connections.clear();
            if (cursor.moveToFirst()) {
                do {
                    String name_from = cursor.getString(0);
                    String name_to = cursor.getString(1);
                    connections.add(new ConnectionHeader(name_from, name_to));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            connectionsLoaded = true;
        }
        return connections;
    }
}
