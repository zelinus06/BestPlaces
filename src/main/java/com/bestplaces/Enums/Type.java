package com.bestplaces.Enums;

public enum Type {
    BEDSIT("Nhà trọ"),
    APARTMENT("Căn hộ"),
    STORE("Cửa hàng"),
    HOUSE("Nhà ở");


    private final String value;

    Type(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
