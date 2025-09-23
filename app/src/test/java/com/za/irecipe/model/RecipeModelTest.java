package com.za.irecipe.model;

import static org.junit.Assert.assertEquals;

import com.za.irecipe.Domain.model.RecipeModel;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class RecipeModelTest {
    private RecipeModel recipeModel;

    @Before
    public void initRecipe() {
        recipeModel = new RecipeModel(
                1,
                "Spaguetti",
                "'Pasta', 'Tomato', 'Cheese'",
                "prepare \n cook \n serve",
                "image.jpg",
                "Main Course",
                500,
                30
        );
    }

    @Test
    public void testRecipeModelIngredients() {
        List<String> ingredients = recipeModel.getIngredientList();
        List<String> expected = Arrays.asList("Pasta", "Tomato", "Cheese");
        assertEquals(expected, ingredients);
    }

    @Test
    public void testRecipeInstruction() {
        List<String> instructions = recipeModel.getInstructionList();
        List<String> expected = Arrays.asList("prepare", "cook", "serve");
        assertEquals(expected, instructions);
    }
}
