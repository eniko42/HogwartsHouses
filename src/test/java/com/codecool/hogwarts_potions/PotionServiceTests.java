package com.codecool.hogwarts_potions;

import com.codecool.hogwarts_potions.model.*;
import com.codecool.hogwarts_potions.service.PotionService;
import com.codecool.hogwarts_potions.service.repositories.*;
import javassist.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
class PotionServiceTests {

    private final PotionService potionService;

    private final PotionDao potionDao;
    private final StudentDao studentDao;
    private final RecipeDao recipeDao;
    private final IngredientDao ingredientDao;
    private final RoomDao roomDao;
    private Potion potion;
    private Student student;
    private Room room;
    List<Ingredient> ingredientList = new ArrayList<>();

    @Autowired
    PotionServiceTests(PotionService potionService, PotionDao potionDao, StudentDao studentDao, RecipeDao recipeDao, IngredientDao ingredientDao, RoomDao roomDao) {
        this.potionService = potionService;
        this.potionDao = potionDao;
        this.studentDao = studentDao;
        this.recipeDao = recipeDao;
        this.ingredientDao = ingredientDao;
        this.roomDao = roomDao;
    }

    @BeforeEach
    void setUp() {
        Ingredient ingredient = new Ingredient("TestIngredient");
        ingredientList.add(ingredient);
        this.room = Room.builder()
                .capacity(4)
                .build();
        roomDao.save(room);
        this.student = Student.builder()
                .name("Harry Potter")
                .houseType(HouseType.GRYFFINDOR)
                .petType(PetType.OWL)
                .room(room)
                .build();
        studentDao.save(student);
        this.potion = Potion.builder()
                .name("Test")
                .brewer(student)
                .status(BrewingStatus.BREW)
                .ingredients(ingredientList)
                .build();
        potionDao.save(potion);
    }

    Potion createPotionWith4Ingredient() {
        Ingredient ingredient1 = new Ingredient("TestIngredient2");
        Ingredient ingredient2 = new Ingredient("TestIngredient3");
        Ingredient ingredient3 = new Ingredient("TestIngredient4");
        Ingredient ingredient4 = new Ingredient("TestIngredient5");
        List<Ingredient> ingredientList = new ArrayList<>(List.of(ingredient1, ingredient2, ingredient3, ingredient4));
        Potion newPotion = Potion.builder().name("TestForDiscovery").brewer(student).status(BrewingStatus.BREW).ingredients(ingredientList).build();
        return potionDao.save(newPotion);
    }

    @Test
    void getAllPotionTest() {
        List<Potion> expected = new ArrayList<>();
        expected.add(potion);
        List<Potion> actual = potionService.getAllPotion();
        assertEquals(expected, actual);
    }

    @Test
    void addPotionTestWithRealStudent() throws NotFoundException {
        Potion expected = Potion.builder().name("Test").status(BrewingStatus.BREW).brewer(student).build();
        Potion actual = potionService.addPotion(Potion.builder().name("Test").build(), expected.getBrewer().getId());
        assertEquals(expected, actual);
    }

    @Test
    void addPotionTestWithNoneExistingStudent() {
        String errorMessage = "Student not found!";
        NotFoundException exception = assertThrows(NotFoundException.class, () -> potionService.addPotion(Potion.builder().name("Test").build(), 0L));
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void getPotionsByStudentTestWithRealStudent() throws NotFoundException {
        List<Potion> expected = List.of(potion);
        List<Potion> actual = potionService.getPotionsByStudent(potion.getBrewer().getId());
        assertEquals(expected, actual);
    }

    @Test
    void getPotionsByStudentTestWithNoneExistingStudent() {
        String errorMessage = "Student not found!";
        NotFoundException exception = assertThrows(NotFoundException.class, () -> potionService.getPotionsByStudent(0L));
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void updatePotionTest() throws NotFoundException {
        Ingredient ingredient = new Ingredient("TestIngredient2");
        ingredientList.add(ingredient);
        potion.setIngredients(ingredientList);
        Potion actual = potionService.updatePotion(ingredient, potion.getId());
        assertEquals(potion, actual);
    }

    @Test
    void updatePotionTestWithNoneExistingPotion() throws NotFoundException {
        Ingredient ingredient = new Ingredient("TestIngredient2");
        ingredientList.add(ingredient);
        potion.setIngredients(ingredientList);
        NotFoundException exception = assertThrows(NotFoundException.class, () -> potionService.updatePotion(ingredient, 0L));
        assertEquals("Potion not found!", exception.getMessage());
    }

    @Test
    void updatePotionTestWithDiscovery() throws NotFoundException {
        Potion potion = createPotionWith4Ingredient();
        Ingredient ingredient = new Ingredient("TestIngredient2");
        potionService.updatePotion(ingredient, potion.getId());
        assertEquals(BrewingStatus.DISCOVERY, potion.getStatus());
    }

    @Test
    void getRecipesByIngredientsTest() {
        List<Recipe> recipes = new ArrayList<>();
        assertEquals(recipes, potionService.getRecipesByIngredients(potion.getId()));
    }

}
