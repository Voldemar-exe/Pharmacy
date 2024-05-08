package com.example.pharmacy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "medicine")
public class Medicine {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private final int imageResource;
    private String description;
    private final double price;

    public Medicine(int imageResource, String description, double price) {
        this.imageResource = imageResource;
        this.description = description;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getImageResource() {
        return imageResource;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
