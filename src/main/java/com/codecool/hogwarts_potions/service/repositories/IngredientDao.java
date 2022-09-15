package com.codecool.hogwarts_potions.service.repositories;

import com.codecool.hogwarts_potions.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientDao extends JpaRepository<Ingredient, Long> {
    Optional<Ingredient> findByName(String name);
}
