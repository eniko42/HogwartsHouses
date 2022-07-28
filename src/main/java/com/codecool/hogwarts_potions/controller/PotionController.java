package com.codecool.hogwarts_potions.controller;

import com.codecool.hogwarts_potions.model.Potion;
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

    @PostMapping("/{student_id}")
    public Potion addPotion(@RequestBody Potion potion, @PathVariable("student_id") Long student_id) {
        return potionService.addPotion(potion, student_id);
    }
}
