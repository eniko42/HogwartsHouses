package com.codecool.hogwarts_potions.controller;

import com.codecool.hogwarts_potions.model.Ingredient;
import com.codecool.hogwarts_potions.model.Potion;
import com.codecool.hogwarts_potions.model.Recipe;
import com.codecool.hogwarts_potions.service.PotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/potions")
public class PotionController {

    PotionService potionService;

    @Autowired
    public PotionController(PotionService potionService) {
        this.potionService = potionService;
    }

    @GetMapping
    public List<Potion> getAllPotions() {
        return potionService.getAllPotion();
    }

    @GetMapping("/{student_id}")
    public List<Potion> getPotionsByStudent(@PathVariable("student_id") Long student_id) {
        return potionService.getPotionsByStudent(student_id);
    }

    @PostMapping("/brew/{student_id}")
    public Potion addPotion(@RequestBody Potion potion, @PathVariable("student_id") Long student_id) {
        return potionService.addPotion(potion, student_id);
    }

    @PutMapping("/potions/{potion_id}/add")
    public Potion updatePotion(@PathVariable("potion_id") Long potion_id, @RequestBody Ingredient ingredient) {
        return potionService.updatePotion(ingredient, potion_id);
    }

    @GetMapping("/potions/{potion-id}/help")
    public List<Recipe> getRecipesByIngredients(@PathVariable("potion-id") Long potion_id) {
        return potionService.getRecipesByIngredients(potion_id);
    }
}
