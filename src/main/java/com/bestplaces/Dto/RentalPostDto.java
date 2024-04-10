package com.bestplaces.Dto;

import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;
import lombok.Getter;

public class RentalPostDto {
    @Getter
    private User id;
    @Getter
    private String city;
    @Getter
    private String district;
    @Getter
    private String commune;
    @Getter
    private String imagePath;
    @Getter
    private int price;
    @Getter
    private int area;
    @Getter
    private Type type;
    @Getter
    private String description;
    @Getter
    private String phoneNumber;
    @Getter
    private String exactAddress;
    private String title;

    public RentalPostDto() {};

    public RentalPostDto(User id, String title, String city, String district, String commune, String imagePath, int price, int area, Type type, String description, String phoneNumber, String exactAddress) {
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
        this.title = title;
    }

    public void setId(User id) {
        this.id = id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public void setCommune(String commune) {
        this.commune = commune;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setExactAddress(String exactAddress) {
        this.exactAddress = exactAddress;
    }

    public void setUser(User user) {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
