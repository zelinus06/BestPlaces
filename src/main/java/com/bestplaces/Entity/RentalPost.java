package com.bestplaces.Entity;

import com.bestplaces.Enums.Type;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "RentalPost")
public class RentalPost {
    @Getter
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
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
    private String exactAddress;
    private String imagepath;
    @Getter
    @Column(nullable = false)
    private double price;
    @Getter
    @Column(nullable = false)
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

    public RentalPost(User userId, String title, String city, String district, String commune, String exactAddress, String imagepath, int price, int area, Type type, String description, String phoneNumber) {
        this.city = city;
        this.district = district;
        this.commune = commune;
        this.exactAddress = exactAddress;
        this.userId = userId;
        this.imagepath = imagepath;
        this.price = price;
        this.area = area;
        this.type = type;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.title = title;
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

    public String getImagePath() {
        return imagepath;
    }

    public void setImagePath(String imagePath) {
        this.imagepath = imagePath;
    }

    public void setPrice(double price) {
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
        this.userId = user;
    }
}

