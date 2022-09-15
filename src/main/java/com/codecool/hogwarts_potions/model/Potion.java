package com.codecool.hogwarts_potions.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "POTION")
public class Potion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "brewer_id")
    private Student brewer;

    @ManyToMany
    private List<Ingredient> ingredients;

    @Enumerated(EnumType.STRING)
    private BrewingStatus status;

    @OneToOne
    private Recipe recipe;

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Potion potion = (Potion) o;
        return Objects.equals(name, potion.name) && Objects.equals(brewer, potion.brewer) && Objects.equals(ingredients, potion.ingredients) && status == potion.status && Objects.equals(recipe, potion.recipe);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brewer, ingredients, status, recipe);
    }
}
