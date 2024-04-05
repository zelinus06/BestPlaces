package com.bestplaces.Dto;

import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;

public class RentalPostDto {
    private User id;
    private String city;
    private String district;
    private String commune;
    private String imagePath;
    private int price;
    private int area;
    private Type type;
    private String description;
    private String phoneNumber;
    private String exactAddress;

    public RentalPostDto() {};

    public RentalPostDto(User id, String city, String district, String commune, String imagePath, int price, int area, Type type, String description, String phoneNumber, String exactAddress) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.commune = commune;
        this.imagePath = imagePath;
        this.price = price;
        this.area = area;
        this.type = type;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.exactAddress = exactAddress;
    }

    public User getId() {
        return id;
    }

    public void setId(User id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCommune() {
        return commune;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

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

    public String getExactAddress() {
        return exactAddress;
    }

    public void setExactAddress(String exactAddress) {
        this.exactAddress = exactAddress;
    }

    public void setUser(User user) {
    }
}
