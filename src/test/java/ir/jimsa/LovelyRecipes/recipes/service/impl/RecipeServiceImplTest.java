package ir.jimsa.LovelyRecipes.recipes.service.impl;

import ir.jimsa.LovelyRecipes.config.exception.SystemServiceException;
import ir.jimsa.LovelyRecipes.recipes.model.dto.RecipeDto;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.model.response.RecipeResponse;
import ir.jimsa.LovelyRecipes.recipes.repository.RecipeRepository;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import ir.jimsa.LovelyRecipes.recipes.util.RecipeUtil;
import ir.jimsa.LovelyRecipes.shared.ErrorMessages;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@DisplayName("Test the RecipeServiceImpl class")
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RecipeServiceImplTest {
    @InjectMocks
    RecipeServiceImpl recipeService;
    @Mock
    RecipeRepository recipeRepository;
    @Mock
    RecipeUtil recipeUtil;

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

        MyApiResponse myApiResponse = new MyApiResponse(
                true, "", new Date(), recipeResponse
        );
        response = new ResponseEntity<>(myApiResponse, HttpStatus.CREATED);

    }

    @Test
    @DisplayName("testing creation a new recipe, when everything is okay")
    void testCreateNewRecipe_WhenInputIsValid_ShouldReturnCorrectObject() {
        // Arrange
        when(recipeRepository.findByTitle(anyString())).thenReturn(null);
        when(recipeRepository.save(any(RecipeEntity.class))).thenReturn(recipeEntity);
        when(recipeUtil.convert(any(RecipeRequest.class))).thenReturn(recipeDto);
        when(recipeUtil.convert(any(RecipeDto.class))).thenReturn(recipeEntity);
        when(recipeUtil.convert(any(RecipeEntity.class))).thenReturn(recipeResponse);
        when(recipeUtil.createResponse(any(RecipeResponse.class), any(HttpStatus.class))).thenReturn(response);

        // Act
        ResponseEntity<MyApiResponse> response = recipeService.createNewRecipe(new RecipeRequest());

        // Assert
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);

        RecipeResponse recipeResponse = (RecipeResponse) response.getBody().getResult();
        assertEquals(recipeResponse.getTitle(), title, "");
        assertEquals(recipeResponse.getInstructions(), instructions);
        assertEquals(recipeResponse.getConsumers(), consumers);
        assertEquals(recipeResponse.isVegetarian(), vegetarian);
    }

    @Test
    @DisplayName("testing creation a new recipe, when recipe existed")
    void testCreateNewRecipe_WhenInputIsExist_ShouldReturnException() {
        // Arrange
        when(recipeRepository.findByTitle(any())).thenReturn(new RecipeEntity());

        // Assert
        SystemServiceException systemServiceException = assertThrows(SystemServiceException.class, () -> {
            // Act
            ResponseEntity<MyApiResponse> response = recipeService.createNewRecipe(new RecipeRequest());
        });
        assertEquals(systemServiceException.getHttpStatus(), HttpStatus.CONFLICT);
        assertEquals(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage(), systemServiceException.getException());
    }

    @Test
    @DisplayName("testing creation a new recipe, when recipe invalid")
    void testCreateNewRecipe_WhenInputIsInvalid_ShouldReturnException() {
        // Arrange
        when(recipeRepository.findByTitle(any())).thenReturn(null);
        when(recipeUtil.convert(any(RecipeRequest.class))).thenReturn(null);

        // Assert
        SystemServiceException systemServiceException = assertThrows(SystemServiceException.class, () -> {
            // Act
            ResponseEntity<MyApiResponse> response = recipeService.createNewRecipe(new RecipeRequest());
        });
        assertEquals(systemServiceException.getHttpStatus(), HttpStatus.UNPROCESSABLE_ENTITY);
        assertEquals(ErrorMessages.INVALID_INPUT_VALUE.getErrorMessage(), systemServiceException.getException());
    }

    @Test
    @DisplayName("testing creation a new recipe, when recipe can not store")
    void testCreateNewRecipe_WhenRecipeStoreFailed_ShouldReturnException() {
        // Arrange
        when(recipeRepository.findByTitle(any())).thenReturn(null);
        when(recipeUtil.convert(any(RecipeRequest.class))).thenReturn(new RecipeDto());
        when(recipeUtil.convert(any(RecipeDto.class))).thenReturn(null);
        when(recipeRepository.save(any())).thenReturn(new SystemServiceException());

        // Assert
        SystemServiceException systemServiceException = assertThrows(SystemServiceException.class, () -> {
            // Act
            ResponseEntity<MyApiResponse> response = recipeService.createNewRecipe(new RecipeRequest());
        });
        assertEquals(systemServiceException.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(ErrorMessages.DATABASE_IO_EXCEPTION.getErrorMessage(), systemServiceException.getException());
    }

    @Test
    @DisplayName("testing delete recipe, when input not found")
    void testDeleteRecipe_WhenInputNotFound_ShouldReturnException() {
        // Arrange
        when(recipeRepository.findByPublicId(any())).thenReturn(null);

        // Assert
        SystemServiceException systemServiceException = assertThrows(SystemServiceException.class, () -> {
            // Act
            ResponseEntity<MyApiResponse> response = recipeService.deleteRecipe(any());
        });

        assertEquals(systemServiceException.getHttpStatus(), HttpStatus.NOT_FOUND);
        assertEquals(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), systemServiceException.getException());
    }

    @Test
    @DisplayName("testing delete recipe, when can not store")
    void testDeleteRecipe_WhenRecipeStoreFailed_ShouldReturnException() {
        // Arrange
        when(recipeRepository.findByPublicId(any())).thenReturn(new RecipeEntity());
        doThrow(SystemServiceException.class)
                .when(recipeRepository)
                .delete(any(RecipeEntity.class));

        // Assert
        SystemServiceException systemServiceException = assertThrows(SystemServiceException.class, () -> {
            // Act
            ResponseEntity<MyApiResponse> response = recipeService.deleteRecipe(any());
        });

        assertEquals(systemServiceException.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(ErrorMessages.DATABASE_IO_EXCEPTION.getErrorMessage(), systemServiceException.getException());
    }

    @Test
    @DisplayName("testing delete recipe, when everything is ok")
    void testDeleteRecipe_WhenInputIsValid_ShouldReturnCorrectObject() {
        // Arrange
        when(recipeRepository.findByPublicId(any())).thenReturn(new RecipeEntity());
        when(recipeUtil.createResponse(any(), any())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Act
        ResponseEntity<MyApiResponse> response = recipeService.deleteRecipe(any());
        MyApiResponse apiResponse = response.getBody();
        HttpStatus httpStatus = response.getStatusCode();

        // Assert
        assertNull(apiResponse);
        assertEquals(HttpStatus.OK, httpStatus);
    }

    @Test
    @DisplayName("testing edit recipe, when everything is ok")
    void testEditRecipe_WhenInputIsValid_ShouldReturnCorrectObject() {
        // Arrange
        when(recipeRepository.findByPublicId(any())).thenReturn(new RecipeEntity());
        when(recipeUtil.update(any(), any())).thenReturn(new RecipeEntity());
        when(recipeRepository.save(any())).thenReturn(new RecipeEntity());
        when(recipeUtil.convert(any(RecipeEntity.class))).thenReturn(new RecipeResponse());
        when(recipeUtil.createResponse(any(), any())).thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        // Act
        ResponseEntity<MyApiResponse> response = recipeService.editRecipe("pid", new RecipeRequest());

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("testing edit recipe, when input not found!")
    void testEditRecipe_WhenInputNotFound_ShouldReturnException() {
        // Arrange
        when(recipeRepository.findByPublicId(any())).thenReturn(null);

        // Assert
        SystemServiceException systemServiceException = assertThrows(SystemServiceException.class, () -> {
            // Act
            ResponseEntity<MyApiResponse> response = recipeService.editRecipe("pid", new RecipeRequest());
        });

        assertEquals(systemServiceException.getHttpStatus(), HttpStatus.NOT_FOUND);
        assertEquals(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), systemServiceException.getException());
    }

    @Test
    @DisplayName("testing edit recipe, when input is not valid!")
    void testEditRecipe_WhenInputIsNotValid_ShouldReturnException() {
        // Arrange
        when(recipeRepository.findByPublicId(any())).thenReturn(new RecipeEntity());
        when(recipeUtil.update(any(), any())).thenReturn(null);

        // Assert
        SystemServiceException systemServiceException = assertThrows(SystemServiceException.class, () -> {
            // Act
            ResponseEntity<MyApiResponse> response = recipeService.editRecipe("pid", new RecipeRequest());
        });

        assertEquals(systemServiceException.getHttpStatus(), HttpStatus.UNPROCESSABLE_ENTITY);
        assertEquals(ErrorMessages.INVALID_INPUT_VALUE.getErrorMessage(), systemServiceException.getException());
    }

    @Test
    @DisplayName("testing edit recipe, when can not store")
    void testEditRecipe_WhenRecipeStoreFailed_ShouldReturnException() {
        // Arrange
        when(recipeRepository.findByPublicId(any())).thenReturn(new RecipeEntity());
        when(recipeUtil.update(any(), any())).thenReturn(new RecipeEntity());
        doThrow(SystemServiceException.class)
                .when(recipeRepository)
                .save(any());

        // Assert
        SystemServiceException systemServiceException = assertThrows(SystemServiceException.class, () -> {
            // Act
            ResponseEntity<MyApiResponse> response = recipeService.editRecipe("pid", new RecipeRequest());
        });

        assertEquals(systemServiceException.getHttpStatus(), HttpStatus.INTERNAL_SERVER_ERROR);
        assertEquals(ErrorMessages.DATABASE_IO_EXCEPTION.getErrorMessage(), systemServiceException.getException());
    }

    @Test
    @DisplayName("testing get recipe, when everything is ok")
    void testGetRecipe_WhenInputIsValid_ShouldReturnCorrectObject() {
        // Arrange
        when(recipeRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(new ArrayList<>()));
        when(recipeUtil.convert(anyList())).thenReturn(new ArrayList<>());
        when(recipeUtil.createResponse(any(), any())).thenReturn(new ResponseEntity<>(
                null, HttpStatus.OK
        ));

        // Act
        int page = 10;
        int size = 100;
        ResponseEntity<MyApiResponse> response = recipeService.getRecipes(page, size);

        // Assert
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertNull(response.getBody());
    }

    @Test
    @DisplayName("testing search recipe, when everything is ok")
    void testSearchRecipe_WhenInputIsValid_ShouldReturnCorrectObject() {
        when(recipeRepository.findAllByConsumers(anyInt())).thenReturn(new ArrayList<>());
        when(recipeUtil.convert(anyList())).thenReturn(new ArrayList<>());
        when(recipeUtil.createResponse(any(), any())).thenReturn(new ResponseEntity<>(
                new MyApiResponse(
                        true, "", new Date(), new ArrayList<>()
                ), HttpStatus.OK
        ));

        int consumers = 1;
        boolean vegetarian = false;
        String include = "A";
        String exclude = "B";
        String instructions = "milk";

        ResponseEntity<MyApiResponse> response = recipeService.searchRecipes(consumers, vegetarian, include, exclude, instructions);

        assertNotNull(response);
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isAction());

    }
}