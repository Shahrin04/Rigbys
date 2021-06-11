package com.example.rigbys.Model;

public class Users {
    private String name, phone, password, profile_image, address, phone_order;

    public Users(){

    }

    public Users(String name, String phone, String password, String profile_image, String address, String phone_order) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.profile_image = profile_image;
        this.address = address;
        this.phone_order = phone_order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_order() {
        return phone_order;
    }

    public void setPhone_order(String phone_order) {
        this.phone_order = phone_order;
    }
}
