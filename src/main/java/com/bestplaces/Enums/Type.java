package com.bestplaces.Enums;

public enum Type {
    BEDSIT("BEDSIT"),
    APARTMENT("APARTMENT"),
    STORE("STORE"),
    HOUSE("HOUSE");


    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
