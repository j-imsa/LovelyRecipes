package ir.jimsa.LovelyRecipes.recipes.util;

import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.shared.Utils;
import org.junit.jupiter.api.*;

import javax.validation.constraints.AssertTrue;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DisplayName("Test the RecipeUtil class")
class RecipeUtilTest {

    private static RecipeUtil recipeUtil;
    private static Utils utils;

    @BeforeAll
    static void setup() {
        utils = new Utils();
        recipeUtil = new RecipeUtil(utils);
    }

    @Test
    void testConvert_WhenInputIsNull_ShouldReturnNull() {
        RecipeRequest recipeRequest = null;
        assertTrue(recipeUtil.convert(recipeRequest) == null);
    }
}