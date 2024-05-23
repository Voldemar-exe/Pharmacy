package com.example.pharmacy.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Entity(tableName = "medicine")
public class Medicine implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private final String name;
    private String description;
    private final String dosage;
    private final String sideEffects;
    private final String interactions;
    private final String type;

    public Medicine(
            String name,
            String description,
            String dosage,
            String sideEffects,
            String interactions,
            String type
    ) {
        this.name = name;
        this.description = description;
        this.dosage = dosage;
        this.sideEffects = sideEffects;
        this.interactions = interactions;
        this.type = type;
    }

    public Medicine(Map<String, Object> data) {
        this.id = (Long) data.get("id");
        this.name = (String) data.get("name");
        this.description = (String) data.get("description");
        this.dosage = (String) data.get("dosage");
        this.sideEffects = (String) data.get("sideEffects");
        this.interactions = (String) data.get("interactions");
        this.type = (String) data.get("type");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Medicine medicine = (Medicine) o;
        return id == medicine.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String getType() {
        return type;
    }

    public String getDosage() {
        return dosage;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public String getInteractions() {
        return interactions;
    }

    public String getName() {
        return name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

}
