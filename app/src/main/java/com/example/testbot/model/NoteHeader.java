package com.example.testbot.model;

public class NoteHeader {

    private String varName;
    private String val;
    private long date;

    public NoteHeader(String varName, String val, long date) {
        this.varName = varName;
        this.val = val;
        this.date = date;
    }

    public String getVarName() {
        return varName;
    }

    public void setVarName(String varName) {
        this.varName = varName;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
