package com.bestplaces.Enums;

public enum Role {
    ADMIN("ADMIN"),
    USER("USER"),
    NONUSER("NONUSER"),;

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
