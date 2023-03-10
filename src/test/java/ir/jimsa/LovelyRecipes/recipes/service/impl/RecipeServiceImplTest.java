package ir.jimsa.LovelyRecipes.recipes.service.impl;

import ir.jimsa.LovelyRecipes.recipes.model.dto.RecipeDto;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.model.response.RecipeResponse;
import ir.jimsa.LovelyRecipes.recipes.repository.RecipeRepository;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import ir.jimsa.LovelyRecipes.recipes.util.RecipeUtil;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("Test the RecipeServiceImpl class")
class RecipeServiceImplTest {

    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeUtil recipeUtil;

    @InjectMocks
    RecipeServiceImpl recipeService;

    RecipeEntity recipeEntity;
    RecipeDto recipeDto;
    RecipeResponse recipeResponse;
    ResponseEntity<MyApiResponse> response;

    String title;
    int consumers;
    boolean vegetarian;
    String instructions;

    @BeforeEach
    void init() {
        title = "test title";
        consumers = 2;
        vegetarian = false;
        instructions = "a, b than c";

        recipeEntity = new RecipeEntity();
        recipeEntity.setTitle(title);
        recipeEntity.setConsumers(consumers);
        recipeEntity.setVegetarian(vegetarian);
        recipeEntity.setInstructions(instructions);

        recipeDto = new RecipeDto();
        recipeDto.setTitle(title);
        recipeDto.setConsumers(consumers);
        recipeDto.setVegetarian(vegetarian);
        recipeDto.setInstructions(instructions);

        recipeResponse = new RecipeResponse();
        recipeResponse.setTitle(title);
        recipeResponse.setConsumers(consumers);
        recipeResponse.setVegetarian(vegetarian);
        recipeResponse.setInstructions(instructions);

        recipeRepository = mock(RecipeRepository.class);
        recipeUtil = mock(RecipeUtil.class);
        recipeService = new RecipeServiceImpl(recipeUtil, recipeRepository);
        response = new ResponseEntity<MyApiResponse>(new MyApiResponse(
                true, "", new Date(), recipeResponse
        ), HttpStatus.CREATED);
    }

    @Test
    @DisplayName("testing creation a new recipe, when everything is okay")
    void testCreateNewRecipe_WhenInputIsValid_ShouldReturnCorrectObject() {
        // Arrange
        Mockito.when(recipeRepository.save(Mockito.any(RecipeEntity.class)))
                .thenReturn(recipeEntity);
        Mockito.when(recipeUtil.convert(Mockito.any(RecipeRequest.class)))
                .thenReturn(recipeDto);
        Mockito.when(recipeUtil.convert(Mockito.any(RecipeDto.class)))
                .thenReturn(recipeEntity);
        Mockito.when(recipeUtil.convert(Mockito.any(RecipeEntity.class)))
                .thenReturn(recipeResponse);
        Mockito.when(recipeUtil.createResponse(Mockito.any(RecipeResponse.class), Mockito.any(HttpStatus.class)))
                .thenReturn(response);

        // Act
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setTitle(title);
        recipeRequest.setInstructions(instructions);
        recipeRequest.setVegetarian(vegetarian);
        recipeRequest.setConsumers(consumers);
        recipeRequest.setIngredients(Arrays.asList("A", "B", "C"));

        ResponseEntity<MyApiResponse> response = recipeService.createNewRecipe(new RecipeRequest());

        // Assert
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        RecipeResponse recipeResponse = (RecipeResponse) response.getBody().getResult();
        assertEquals(recipeResponse.getTitle(), title);
        assertEquals(recipeResponse.getInstructions(), instructions);
        assertEquals(recipeResponse.getConsumers(), consumers);
        assertEquals(recipeResponse.isVegetarian(), vegetarian);
    }

}