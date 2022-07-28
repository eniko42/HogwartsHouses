package com.codecool.hogwarts_potions.service;

import com.codecool.hogwarts_potions.model.BrewingStatus;
import com.codecool.hogwarts_potions.model.Ingredient;
import com.codecool.hogwarts_potions.model.Potion;
import com.codecool.hogwarts_potions.model.Recipe;
import com.codecool.hogwarts_potions.service.repositories.IngredientDao;
import com.codecool.hogwarts_potions.service.repositories.PotionDao;
import com.codecool.hogwarts_potions.service.repositories.RecipeDao;
import com.codecool.hogwarts_potions.service.repositories.StudentDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PotionService {

    PotionDao potionDao;
    StudentDao studentDao;
    RecipeDao recipeDao;
    IngredientDao ingredientDao;

    @Autowired
    public PotionService(PotionDao potionDao, StudentDao studentDao, RecipeDao recipeDao, IngredientDao ingredientDao) {
        this.potionDao = potionDao;
        this.studentDao = studentDao;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
    }

    public List<Potion> getAllPotion() {
        return potionDao.findAll();
    }

    public Potion addPotion(Potion potion, Long student_id) {
        potion.setBrewer(studentDao.findById(student_id).get());
        setIngredients(potion);
        setStatus(potion);
        setRecipe(potion);
        return potionDao.save(potion);
    }

    private void setIngredients(Potion potion) {
        List<Ingredient> ingredientsToSet = new ArrayList<>();
        potion.getIngredients().forEach(ingredient -> ingredientsToSet.add(saveNewIngredients(ingredient)));
        potion.setIngredients(ingredientsToSet);
    }


    private Ingredient saveNewIngredients(Ingredient ingredient) {
        final Ingredient savedIngredient = ingredientDao.findByName(ingredient.getName());
        if (savedIngredient == null) {
            return ingredientDao.save(ingredient);
        } else return savedIngredient;
    }

    private void setRecipe(Potion potion) {
        if (potion.getStatus() == BrewingStatus.REPLICA) {
            potion.setRecipe(getExistingRecipe(potion.getIngredients()).get());
        }
        else if (potion.getStatus() == BrewingStatus.DISCOVERY) {
            Recipe recipe = new Recipe();
            recipe.setName(potion.getBrewer().getName() + "'s discovery");
            recipe.setBrewer(potion.getBrewer());
            recipe.setIngredients(potion.getIngredients());
            potion.setRecipe(recipeDao.save(recipe));
        }
    }

    private void setStatus(Potion potion) {
        List<Ingredient> ingredients = potion.getIngredients();
        if (notEnoughIngredients(ingredients)) {
            potion.setStatus(BrewingStatus.BREW);
        }
        else if (getExistingRecipe(ingredients).isEmpty()) {
            potion.setStatus(BrewingStatus.DISCOVERY);
        }
        else {
            potion.setStatus(BrewingStatus.REPLICA);
        }
    }

    private Optional<Recipe> getExistingRecipe(List<Ingredient> ingredients) {
        List<Recipe> recipes = recipeDao.findAll();
        return recipes.stream().filter(recipe -> recipe.getIngredients().containsAll(ingredients)).findFirst();
    }

    private boolean notEnoughIngredients(List<Ingredient> ingredients) {
        return ingredients.size() < Recipe.MINIMAL_NUMBER_OF_INGREDIENTS;
    }

    public List<Potion> getPotionsByStudent(Long student_id) {
        return potionDao.findByBrewer_Id(student_id);
    }
}
