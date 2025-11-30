package com.example.testbot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.testbot.model.ConnectionHeader;
import com.example.testbot.model.NoteHeader;
import com.example.testbot.model.VariableHeader;
import com.example.testbot.model.VariableType;

import java.util.ArrayList;

public class DataAccessObject {
    private static DataAccessObject instance;
    private final DatabaseHelper helper;

    public final ArrayList<VariableHeader> variables = new ArrayList<>();
    public final ArrayList<ConnectionHeader> connections = new ArrayList<>();
    public final ArrayList<NoteHeader> notes = new ArrayList<>();
    private boolean notesLoaded = false;

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
                    instance.getAllNotes();
                }
            }
        }
        return instance;
    }

    public void newNote(NoteHeader note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("var_name", note.getVarName());
        values.put("val", note.getVal());
        values.put("date", note.getDate());
        db.insert("note", null, values);
        db.close();

        notes.add(note);
    }
    public void updateNote(NoteHeader note) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("val", note.getVal());
        values.put("date", note.getDate());

        db.update("note", values, "var_name=?", new String[]{note.getVarName()});
        db.close();
    }

    public void deleteNote(String varName) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete("note", "var_name=?", new String[]{varName});
        db.close();

        for (int i = 0; i < notes.size(); i++) {
            if (notes.get(i).getVarName().equals(varName)) {
                notes.remove(i);
                break;
            }
        }
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

    public ArrayList<NoteHeader> getAllNotes() {
        if (!notesLoaded) {
            SQLiteDatabase db = helper.getReadableDatabase();
            Cursor cursor = db.query(
                    "note",
                    new String[]{"var_name", "val", "date"},
                    null, null, null, null, "date DESC"
            );

            notes.clear();
            if (cursor.moveToFirst()) {
                do {
                    String varName = cursor.getString(0);
                    String val = cursor.getString(1);
                    long date = cursor.getLong(2);

                    notes.add(new NoteHeader(varName, val, date));
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
            notesLoaded = true;
        }

        return notes;
    }

    public ArrayList<NoteHeader> getNotesForVariableLocal(String varName) {
        ArrayList<NoteHeader> result = new ArrayList<>();
        for (NoteHeader n : notes) {
            if (n.getVarName().equals(varName)) {
                result.add(n);
            }
        }
        result.sort((a, b) -> {
            if (a.getDate() > b.getDate()) {
                return 1;
            } else if (a.getDate() == b.getDate()) {
                return 0;
            } else {
                return -1;
            }
        });
        return result;
    }

    public double[] toDoubleSeries(ArrayList<NoteHeader> list, VariableType type) {

        double[] out = new double[list.size()];

        for (int i = 0; i < list.size(); i++) {
            String v = list.get(i).getVal();

            switch (type) {
                case BOOLEAN:
                    if (v.equals("1")) {
                        out[i] = 1.0;
                    } else {
                        out[i] = 0.0;
                    }
                    break;

                case RANGE:
                case TIME:
                default:
                    try {
                        out[i] = Double.parseDouble(v);
                    } catch (Exception e) {
                        out[i] = 0.0;
                    }
                    break;
            }
        }
        return out;
    }

    public boolean[] allDefined(int n) {
        boolean[] d = new boolean[n];
        for (int i = 0; i < n; i++) d[i] = true;
        return d;
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
