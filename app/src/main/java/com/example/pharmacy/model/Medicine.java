package com.example.pharmacy.model;

public class Medicine {
    private final int imageResource;
    private final String description;
    private final double price;

    public Medicine(int imageResource, String description, double price) {
        this.imageResource = imageResource;
        this.description = description;
        this.price = price;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
