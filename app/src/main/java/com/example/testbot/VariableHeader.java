package com.example.testbot;

public class VariableHeader {

    private String name;
    private VariableType unit;

    public VariableHeader(String name, VariableType unit) {
        this.name = name;
        this.unit = unit;
    }

    public String getName() { return name; }
    public VariableType getUnit() { return unit; }
}
