package com.example.HOSIX.AGiRH.Enum;

public enum Statut {
    ACTIF("Actif"),
    INACTIF("Inactif"),
    EN_CONGE("En Congé");

    private final String description;

    Statut(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
