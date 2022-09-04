package com.codecool.hogwarts_potions.model;

import java.util.List;

public enum PetType {
    CAT,
    RAT,
    OWL,
    NONE;


    public static List<PetType> dangerousPetsForRatOwners() {
        return List.of(PetType.OWL, PetType.CAT);
    }
}
