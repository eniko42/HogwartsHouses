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
import java.util.stream.Collectors;

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
        potion.setStatus(BrewingStatus.BREW);
        return potionDao.save(potion);
    }


    private void checkIfNeedRecipe(Potion potion) {
        if (potion.getStatus() != BrewingStatus.BREW) {
            setRecipe(potion);
        }
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
            potion.setRecipe(getExistingRecipe(potion.getIngredients()).get(1));
        } else if (potion.getStatus() == BrewingStatus.DISCOVERY) {
            Recipe recipe = new Recipe();
            recipe.setName(potion.getBrewer().getName() + "'s discovery");
            recipe.setBrewer(potion.getBrewer());
            List<Ingredient> ingredients = new ArrayList<>(potion.getIngredients());
            recipe.setIngredients(ingredients);
            potion.setRecipe(recipeDao.save(recipe));
        }
    }

    private void setStatus(Potion potion) {
        List<Ingredient> ingredients = potion.getIngredients();
        if (notEnoughIngredients(ingredients)) {
            potion.setStatus(BrewingStatus.BREW);
        } else if (getExistingRecipe(ingredients).isEmpty()) {
            potion.setStatus(BrewingStatus.DISCOVERY);
        } else {
            potion.setStatus(BrewingStatus.REPLICA);
        }
    }

    private List<Recipe> getExistingRecipe(List<Ingredient> ingredients) {
        List<Recipe> recipes = recipeDao.findAll();
        return recipes.stream().filter(recipe -> recipe.getIngredients().containsAll(ingredients)).collect(Collectors.toList());
    }

    private boolean notEnoughIngredients(List<Ingredient> ingredients) {
        return ingredients.size() < Recipe.MINIMAL_NUMBER_OF_INGREDIENTS;
    }

    public List<Potion> getPotionsByStudent(Long student_id) {
        return potionDao.findByBrewer_Id(student_id);
    }

    public Potion updatePotion(Ingredient ingredient, Long potion_id) {
        Potion potion = potionDao.findById(potion_id).get();
        potion.addIngredient(saveNewIngredients(ingredient));
        setStatus(potion);
        checkIfNeedRecipe(potion);
        return potionDao.save(potion);
    }

    public List<Recipe> getRecipesByIngredients(Long potion_id) {
        List<Ingredient> ingredients = potionDao.getById(potion_id).getIngredients();
        return getExistingRecipe(ingredients);
    }
}
