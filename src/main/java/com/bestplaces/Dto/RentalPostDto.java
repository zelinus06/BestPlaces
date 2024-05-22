package com.bestplaces.Dto;

import com.bestplaces.Entity.User;
import com.bestplaces.Enums.Type;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
public class RentalPostDto {
    private User id;
    private String city;
    private String district;
    private String commune;
    private int price;
    private int area;
    private Type type;
    private String description;
    private String phoneNumber;
    private String street;
    private String numberHouse;
    private String title;

    public RentalPostDto(User id, String title, String city, String district, String commune, int price, int area, Type type, String description, String phoneNumber, String street, String numberHouse) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.commune = commune;
        this.price = price;
        this.area = area;
        this.type = type;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.title = title;
        this.numberHouse = numberHouse;
    }

    public RentalPostDto(User id, String city, String district, String commune, Type type, String phoneNumber, String street, String numberHouse) {
        this.id = id;
        this.city = city;
        this.district = district;
        this.commune = commune;
        this.type = type;
        this.phoneNumber = phoneNumber;
        this.street = street;
        this.numberHouse = numberHouse;
    }
}
