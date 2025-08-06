package com.example.HOSIX.AGiRH.Enum;

public enum TypeService {
    MEDICAL("Service Médical"),
    CHIRURGICAL("Service Chirurgical");

    private final String description;

    TypeService(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
