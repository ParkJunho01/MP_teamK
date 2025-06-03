package com.example.tageatproject.model;

import java.io.Serializable;
import java.util.List;

public class Place implements Serializable {
    private String name;
    private String address;
    private String category;
    private List<String> tags;
    private String imageUrl;
    private String link;
    private double latitude;
    private double longitude;

    public Place() {}

    public Place(String name, String address, String category, List<String> tags, String imageUrl, String link, Double latitude, Double longitude) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.tags = tags;
        this.imageUrl = imageUrl;
        this.link = link;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCategory() {
        return category;
    }

    public List<String> getTags() {
        return tags;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getLink() {
        return link;
    }

    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
}

