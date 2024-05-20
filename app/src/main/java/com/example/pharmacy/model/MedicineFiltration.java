package com.example.pharmacy.model;

import java.io.Serializable;

public class MedicineFiltration implements Serializable {
    private final String searchText;
    private final String[] types;
    private final String dosage;
    private final String sideEffects;
    private final String interaction;

    public MedicineFiltration() {
        this.searchText = null;
        this.types = null;
        this.dosage = null;
        this.sideEffects = null;
        this.interaction = null;
    }

    public MedicineFiltration(String searchText, String[] types, String dosage, String sideEffects, String interaction) {
        this.searchText = searchText;
        this.types = types;
        this.dosage = dosage;
        this.sideEffects = sideEffects;
        this.interaction = interaction;
    }

    public String getSearchText() {
        return searchText;
    }

    public String[] getTypes() {
        return types;
    }

    public String getDosage() {
        return dosage;
    }

    public String getSideEffects() {
        return sideEffects;
    }

    public String getInteraction() {
        return interaction;
    }
}
