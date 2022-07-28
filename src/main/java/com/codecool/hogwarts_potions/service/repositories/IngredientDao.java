package com.codecool.hogwarts_potions.service.repositories;

import com.codecool.hogwarts_potions.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientDao extends JpaRepository<Ingredient, Long> {
    Ingredient findByName(String name);
}
