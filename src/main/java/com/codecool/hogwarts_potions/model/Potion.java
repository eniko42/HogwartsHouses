package com.codecool.hogwarts_potions.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;

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

    private BrewingStatus status;

    @OneToOne
    private Recipe recipe;

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
    }
}
