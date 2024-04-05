package com.bestplaces.Dto;

import lombok.Getter;

public class UserRegistrationDto {
    @Getter
    private String username;
    @Getter
    private String email;
    @Getter
    private String password;
    private String phoneNumber;
    public UserRegistrationDto(){
    }
    public UserRegistrationDto(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}

