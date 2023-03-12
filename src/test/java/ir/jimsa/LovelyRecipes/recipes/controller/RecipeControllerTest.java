package ir.jimsa.LovelyRecipes.recipes.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.jimsa.LovelyRecipes.config.exception.SystemServiceException;
import ir.jimsa.LovelyRecipes.recipes.model.dto.RecipeDto;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.model.response.RecipeResponse;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import ir.jimsa.LovelyRecipes.recipes.service.RecipeService;
import ir.jimsa.LovelyRecipes.recipes.service.impl.RecipeServiceImpl;
import ir.jimsa.LovelyRecipes.shared.ErrorMessages;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = RecipeController.class)
//@MockBean({RecipeServiceImpl.class})
class RecipeControllerTest {

    private String path = "/recipes";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    RecipeService recipeService;

    @Test
    @DisplayName("creat a new recipe")
    void testCreateNewRecipe_WhenInputIsValid_ShouldReturnValidObject() throws Exception {
        // Arrange
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setTitle("title");
        recipeRequest.setVegetarian(false);
        recipeRequest.setConsumers(2);
        recipeRequest.setInstructions("instructions...");
        recipeRequest.setIngredients(Arrays.asList("A", "B", "C"));

        RecipeResponse recipeResponse = new ModelMapper().map(recipeRequest, RecipeResponse.class);

        ResponseEntity responseEntity = new ResponseEntity<>(
                new MyApiResponse(true, "", new Date(), recipeResponse), HttpStatus.CREATED
        );
        when(recipeService.createNewRecipe(any())).thenReturn(responseEntity);


        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(recipeRequest));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();
        MyApiResponse response = new ObjectMapper().readValue(responseBodyAsString, MyApiResponse.class);
        HashMap<String, String> data = (HashMap<String, String>) response.getResult();

        // Assert
        assertNotNull(response);
        assertTrue(response.isAction());
        assertEquals(HttpStatus.CREATED.value(), mvcResult.getResponse().getStatus());
        assertEquals(2, data.get("consumers"));
    }

    @Test
    @DisplayName("creat a new recipe with invalid inputs")
    void testCreateNewRecipe_WhenInputIsInvalid_ShouldReturnException() throws Exception {
        // Arrange
        RecipeRequest recipeRequest = new RecipeRequest();
        recipeRequest.setTitle("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(path)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .content(new ObjectMapper().writeValueAsString(recipeRequest));

        // Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        // Assert
        assertNotNull(responseBodyAsString);
        assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
        assertEquals(responseBodyAsString, "");
    }
}