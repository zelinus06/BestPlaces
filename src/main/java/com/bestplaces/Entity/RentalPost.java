package com.bestplaces.Entity;

import com.bestplaces.Enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RentalPost")
public class RentalPost {
    @Getter
    @Setter
    @Id
    private Long id_post;
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;
    @Getter
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Type type;
    @Getter
    @Column(nullable = false)
    private String city;
    @Getter
    @Column(nullable = false)
    private String district;
    @Getter
    @Column(nullable = false)
    private String commune;
    @Getter
    @Setter
    private String street;
    @Getter
    @Setter
    private String numberHouse;
    @Getter
//    @Column(nullable = false)
    private int price;
    @Getter
//    @Column(nullable = false)
    private int area;
    @Getter
    private String description;
    @Getter
    private String phoneNumber;
    @Getter
    @Setter
    private double latitude;
    @Getter
    @Setter
    private double longtitude;
    @Getter
    @Setter
    private String title;

    public RentalPost(){
    }

//    public RentalPost(User userId, String title, String city, String district, String commune, String street, String numberhouse, int price, int area, Type type, String description, String phoneNumber) {
//        this.city = city;
//        this.district = district;
//        this.commune = commune;
//        this.street = street;
//        this.numberHouse = numberhouse;
//        this.userId = userId;
//        this.price = price;
//        this.area = area;
//        this.type = type;
//        this.description = description;
//        this.phoneNumber = phoneNumber;
//        this.title = title;
//    }

    public RentalPost(User id, String city, String district, String commune, String street, String numberHouse, Type type, String phoneNumber) {
        this.city = city;
        this.district = district;
        this.commune = commune;
        this.street = street;
        this.numberHouse = numberHouse;
        this.userId = userId;
        this.type = type;
        this.phoneNumber = phoneNumber;

    }

    public void setId(User userId) {
        this.userId = userId;
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


    public void setUser(User user) {
        this.userId = user;
    }
}

