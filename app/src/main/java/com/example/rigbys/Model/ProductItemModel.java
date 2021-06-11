package com.example.rigbys.Model;

import android.net.Uri;

public class ProductItemModel {
    private String date, description, id, link, name, price, time, status, category, image;

    public ProductItemModel(){

    }

    public ProductItemModel(String date, String description, String id, String link, String name, String price, String time, String status, String category, String image) {
        this.date = date;
        this.description = description;
        this.id = id;
        this.link = link;
        this.name = name;
        this.price = price;
        this.time = time;
        this.status = status;
        this.category = category;
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
