package ir.jimsa.LovelyRecipes.recipes.service;

import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import org.springframework.http.ResponseEntity;

public interface RecipeService {
    ResponseEntity<MyApiResponse> createNewRecipe(RecipeRequest recipeRequest);

    ResponseEntity<MyApiResponse> deleteRecipe(String publicId);

    ResponseEntity<MyApiResponse> editRecipe(String publicId, RecipeRequest recipeRequest);

    ResponseEntity<MyApiResponse> getRecipes(int page, int size);

    ResponseEntity<MyApiResponse> searchRecipes(int consumers, boolean vegetarian, String include, String exclude, String instructions);
}
