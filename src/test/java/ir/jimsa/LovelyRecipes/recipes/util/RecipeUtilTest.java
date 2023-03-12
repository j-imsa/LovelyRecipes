package ir.jimsa.LovelyRecipes.recipes.util;

import ir.jimsa.LovelyRecipes.recipes.model.dto.IngredientDto;
import ir.jimsa.LovelyRecipes.recipes.model.dto.RecipeDto;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.model.response.RecipeResponse;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.IngredientEntity;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import ir.jimsa.LovelyRecipes.shared.Constants;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import ir.jimsa.LovelyRecipes.shared.Utils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test the RecipeUtil class")
class RecipeUtilTest {
    Utils utils;
    RecipeUtil recipeUtil;
    String title;
    int consumers;
    boolean vegetarian;
    String instructions;
    List<String> ingredients;

    @BeforeEach
    void init() {
        utils = new Utils();
        recipeUtil = new RecipeUtil(utils);

        title = "test title";
        consumers = 2;
        vegetarian = false;
        instructions = "a, b than c";
        ingredients = Arrays.asList("A", "B", "C");
    }

    @Test
    @DisplayName("testing convert by in:request and out:dto by null input")
    void testConvertRequestModelToDtoModel_WhenInputIsNull_ShouldReturnNull() {

        RecipeRequest recipeRequest = null;
        RecipeDto recipeDto = recipeUtil.convert(recipeRequest);

        assertNull(recipeDto, "convert did not return null");
    }

    @Test
    @DisplayName("testing convert by in:request and out:dto by valid input")
    void testConvertRequestModelToDtoModel_WhenInputIsValid_ShouldReturnValidDto() {

        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setTitle(title);
        recipeRequest.setConsumers(consumers);
        recipeRequest.setIngredients(ingredients);
        recipeRequest.setVegetarian(vegetarian);
        recipeRequest.setInstructions(instructions);

        RecipeDto recipeDto = recipeUtil.convert(recipeRequest);

        assertNotNull(recipeDto, "convert did not return null");
        assertEquals(recipeDto.getTitle(), title, "convert did not return right title");
        assertEquals(recipeDto.getInstructions(), instructions, "convert did not return right instructions");
        assertEquals(recipeDto.getConsumers(), consumers, "convert did not return right consumers");
        assertEquals(recipeDto.isVegetarian(), vegetarian, "convert did not return right vegetarian");
        recipeDto.getIngredients()
                .forEach(ingredientDto -> assertTrue(ingredients.stream().anyMatch(s -> s.equals(ingredientDto.getTitle())), "convert did not return right ingredientDto.title"));
        assertNotNull(recipeDto.getPublicId(), "convert did not return valid object");
        assertEquals(Constants.PUBLIC_ID_LENGTH, recipeDto.getPublicId().length(), "convert did not return valid object");
        assertEquals(0, recipeDto.getId(), "convert did not return valid object");
    }

    @Test
    @DisplayName("testing convert by in:request and out:dto by invalid input")
    void testConvertRequestModelToDtoModel_WhenInputIsInValid_ShouldReturnNull() {

        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setTitle("");

        RecipeDto recipeDto = recipeUtil.convert(recipeRequest);

        assertNull(recipeDto, "convert did not return not-null-obj");
    }

    @Test
    @DisplayName("testing convert by in:dto and out:entity by null input")
    void testConvertDtoModelToEntityModel_WhenInputIsNull_ShouldReturnNull() {

        RecipeDto recipeDto = null;

        RecipeEntity recipeEntity = recipeUtil.convert(recipeDto);

        assertNull(recipeEntity, "convert did not return null");
    }

    @Test
    @DisplayName("testing convert by in:dto and out:entity by valid input")
    void testConvertDtoModelToEntityModel_WhenInputIsValid_ShouldReturnValid() {

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setTitle(title);
        recipeDto.setConsumers(consumers);
        recipeDto.setVegetarian(vegetarian);
        recipeDto.setInstructions(instructions);

        List<IngredientDto> ingredientDtos = new ArrayList<>();
        ingredientDtos.add(new IngredientDto(1, "pid1", "title1", recipeDto));
        ingredientDtos.add(new IngredientDto(2, "pid2", "title2", recipeDto));
        ingredientDtos.add(new IngredientDto(3, "pid3", "title3", recipeDto));
        recipeDto.setIngredients(ingredientDtos);

        RecipeEntity recipeEntity = recipeUtil.convert(recipeDto);

        assertNotNull(recipeEntity, "convert return null");
        assertEquals(recipeEntity.getTitle(), title, "convert did not return right title");
        assertEquals(recipeEntity.getInstructions(), instructions, "convert did not return right instructions");
        assertEquals(recipeEntity.getConsumers(), consumers, "convert did not return right consumers");
        assertEquals(recipeEntity.isVegetarian(), vegetarian, "convert did not return right vegetarian");
        assertEquals(3, recipeEntity.getIngredients().size(), "convert did not return right ingredientDtos size");
    }

    @Test
    @DisplayName("testing convert by in:dto and out:entity by invalid input")
    void testConvertDtoModelToEntityModel_WhenInputIsInvalid_ShouldReturnNull() {

        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setIngredients(null);

        RecipeEntity recipeEntity = recipeUtil.convert(recipeDto);

        assertNull(recipeEntity, "convert did not return null");
    }

    @Test
    @DisplayName("testing convert by in:entity and out:response by null input")
    void testConvertEntityModelToResponseModel_WhenInputIsNull_ShouldReturnNull() {

        RecipeEntity recipeEntity = null;

        RecipeResponse recipeResponse = recipeUtil.convert(recipeEntity);

        assertNull(recipeResponse, "convert did not return null");
    }

    @Test
    @DisplayName("testing convert by in:entity and out:response by invalid input")
    void testConvertEntityModelToResponseModel_WhenInputIsInvalid_ShouldReturnNull() {

        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setIngredients(null);

        RecipeResponse recipeResponse = recipeUtil.convert(recipeEntity);

        assertNull(recipeResponse, "convert did not return null");
    }

    @Test
    @DisplayName("testing convert by in:entity and out:response by valid input")
    void testConvertEntityModelToResponseModel_WhenInputIsValid_ShouldReturnValid() {

        RecipeEntity recipeEntity = new RecipeEntity();
        recipeEntity.setTitle(title);
        recipeEntity.setConsumers(consumers);
        recipeEntity.setVegetarian(vegetarian);
        recipeEntity.setInstructions(instructions);

        List<IngredientEntity> ingredientEntities = new ArrayList<>();
        ingredientEntities.add(new IngredientEntity());
        recipeEntity.setIngredients(ingredientEntities);

        RecipeResponse recipeResponse = recipeUtil.convert(recipeEntity);

        assertNotNull(recipeResponse, "convert return null");
        assertEquals(recipeResponse.getTitle(), title, "convert did not return right title");
        assertEquals(recipeResponse.getInstructions(), instructions, "convert did not return right instructions");
        assertEquals(recipeResponse.getConsumers(), consumers, "convert did not return right consumers");
        assertEquals(recipeResponse.isVegetarian(), vegetarian, "convert did not return right vegetarian");
        assertEquals(1, recipeResponse.getIngredients().size(), "convert did not return right ingredientDtos size");

    }

    @Test
    @DisplayName("testing convert by in:lst(entity) and out:lst(response) by valid input")
    void testConvertListOfEntityModelsToListOfResponseModels_WhenInputIsValid_ShouldReturnValid() {

        List<RecipeEntity> recipeEntities = new ArrayList<>();
        recipeEntities.add(new RecipeEntity());

        List<RecipeResponse> recipeResponses = recipeUtil.convert(recipeEntities);

        assertNotNull(recipeResponses, "convert return null");
    }

    @Test
    @DisplayName("testing convert by in:lst(entity) and out:lst(response) by invalid input")
    void testConvertListOfEntityModelsToListOfResponseModels_WhenInputIsInvalid_ShouldReturnEmptyList() {

        List<RecipeEntity> recipeEntities = null;

        // idea: had changed the logic (instead null, I will return empty list)
        List<RecipeResponse> recipeResponses = recipeUtil.convert(recipeEntities);

        assertNotNull(recipeResponses, "convert return null");
        assertEquals(0, recipeResponses.size(), "convert return null");
    }

    @Test
    @DisplayName("testing createResponse by invalid inputs")
    void testCreateResponse_WhenInputIsInvalid_ShouldReturnNull() {

        Object result = null;
        HttpStatus httpStatus = null;
        ResponseEntity<MyApiResponse> response = recipeUtil.createResponse(result, httpStatus);
        assertNull(response, "createResponse did not return null");

        result = null;
        httpStatus = HttpStatus.OK;
        response = recipeUtil.createResponse(result, httpStatus);
        assertNull(response, "createResponse did not return null");

        result = new Object();
        httpStatus = null;
        response = recipeUtil.createResponse(result, httpStatus);
        assertNull(response, "createResponse did not return null");
    }

    @Test
    @DisplayName("testing createResponse by valid inputs")
    void testCreateResponse_WhenInputIsValid_ShouldReturnValid() {

        Object result = new Object();
        HttpStatus httpStatus = HttpStatus.OK;

        ResponseEntity<MyApiResponse> response = recipeUtil.createResponse(result, httpStatus);

        assertNotNull(response, "createResponse return null");
        assertEquals(response.getStatusCode(), httpStatus);
        assertTrue(Objects.requireNonNull(response.getBody()).isAction(), "createResponse return false");
        assertNotNull(response.getBody().getResult(), "createResponse return null result");
    }

    @Test
    @DisplayName("testing update by invalid inputs")
    void testUpdate_WhenInputIsInvalid_ShouldReturnNull() {

        RecipeEntity existedRecipeEntity = null;
        RecipeRequest recipeRequest = null;
        RecipeEntity updatedRecipeEntity = recipeUtil.update(existedRecipeEntity, recipeRequest);
        assertNull(updatedRecipeEntity, "update did not return null");

        existedRecipeEntity = new RecipeEntity();
        recipeRequest = null;
        updatedRecipeEntity = recipeUtil.update(existedRecipeEntity, recipeRequest);
        assertNull(updatedRecipeEntity, "update did not return null");

        existedRecipeEntity = null;
        recipeRequest = new RecipeRequest();
        updatedRecipeEntity = recipeUtil.update(existedRecipeEntity, recipeRequest);
        assertNull(updatedRecipeEntity, "update did not return null");
    }

    @ParameterizedTest
    @MethodSource("updateMethodValidParameters")
    @DisplayName("testing update by valid inputs")
    void testUpdate_WhenInputIsValid_ShouldReturnValid(RecipeEntity existedRecipeEntity, RecipeRequest recipeRequest) {
        RecipeEntity updatedRecipeEntity = recipeUtil.update(existedRecipeEntity, recipeRequest);
        assertNotNull(updatedRecipeEntity, "update return null");
        assertEquals(updatedRecipeEntity.getTitle(), recipeRequest.getTitle(), "update did not return right title");
        assertEquals(updatedRecipeEntity.getConsumers(), recipeRequest.getConsumers(), "update did not return right consumers");
        assertEquals(updatedRecipeEntity.getInstructions(), recipeRequest.getInstructions(), "update did not return right instructions");
        assertEquals(updatedRecipeEntity.isVegetarian(), recipeRequest.isVegetarian(), "update did not return right vegetarian");
    }

    private static Stream<Arguments> updateMethodValidParameters() {
        return Stream.of(
                Arguments.of(new RecipeEntity(), new RecipeRequest("title1", Collections.singletonList("A1, B, C"), "A1, then B, and C", 4, true)),
                Arguments.of(new RecipeEntity(), new RecipeRequest("title2", Collections.singletonList("A2, B, C"), "A2, then B, and C", 1, false)),
                Arguments.of(new RecipeEntity(), new RecipeRequest("title3", Collections.singletonList("A3, B, C"), "A3, then B, and C", 1, true)),
                Arguments.of(new RecipeEntity(), new RecipeRequest("title4", Collections.singletonList("A4, B, C"), "A4, then B, and C", 6, false)),
                Arguments.of(new RecipeEntity(), new RecipeRequest("title5", Collections.singletonList("A5, B, C"), "A5, then B, and C", 2, true))
        );
    }
}