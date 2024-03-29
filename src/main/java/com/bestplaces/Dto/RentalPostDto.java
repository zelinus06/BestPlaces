package com.bestplaces.Dto;

import com.bestplaces.Enums.Type;

public class RentalPostDto {
    private String idUser;
    private String imagePath;
    private int price;
    private int area;
    private Type type;
    private String description;
    private String phoneNumber;
    private String address;
    private String exactAddress;

    public RentalPostDto() {};

    public RentalPostDto(String imagePath, int price, int area, Type type, String description, String phoneNumber, String address, String exactAddress) {
        this.imagePath = imagePath;
        this.price = price;
        this.area = area;
        this.type = type;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.exactAddress = exactAddress;
    }

    // Getters and setters


    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getExactAddress() {
        return exactAddress;
    }

    public void setExactAddress(String exactAddress) {
        this.exactAddress = exactAddress;
    }
}
