package ir.jimsa.LovelyRecipes.recipes.util;

import ir.jimsa.LovelyRecipes.recipes.model.dto.IngredientDto;
import ir.jimsa.LovelyRecipes.recipes.model.dto.RecipeDto;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.model.response.RecipeResponse;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.IngredientEntity;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import ir.jimsa.LovelyRecipes.shared.Utils;

import java.util.Collections;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class RecipeUtil {
    Logger logger = LoggerFactory.getLogger(RecipeUtil.class);
    private final Utils utils;
    private ModelMapper modelMapper = new ModelMapper();

    public RecipeUtil(Utils utils) {
        this.utils = utils;
    }

    public RecipeDto convert(RecipeRequest recipeRequest) {
        if (isValidRequestModel(recipeRequest)) {
            return createDtoModel(recipeRequest);
        }
        return null;
    }

    private RecipeDto createDtoModel(RecipeRequest recipeRequest) {
        RecipeDto recipeDto = modelMapper.map(recipeRequest, RecipeDto.class);
        recipeDto.setPublicId(utils.getPublicId());
        recipeDto.setIngredients(recipeRequest.getIngredients().stream().map(s -> {
            IngredientDto ingredientDto = new IngredientDto();
            ingredientDto.setTitle(s);
            ingredientDto.setPublicId(utils.getPublicId());
            ingredientDto.setRecipeDto(recipeDto);
            return ingredientDto;
        }).collect(Collectors.toList()));
        return recipeDto;
    }

    private boolean isValidRequestModel(RecipeRequest recipeRequest) {
        boolean flag = true;
        if (recipeRequest == null) {
            return false;
        }
        if (recipeRequest.getTitle().isEmpty()) {
            return false;
        }
        if (recipeRequest.getInstructions().isEmpty()) {
            return false;
        }
        // As the assignment had told me codes must be production-ready, I think more than 16 is unnatural
        if (recipeRequest.getConsumers() <= 0 || recipeRequest.getConsumers() >= 17) {
            return false;
        }
        if (recipeRequest.getIngredients() == null || recipeRequest.getIngredients().isEmpty()) {
            return false;
        }
        return flag;
    }

    public RecipeEntity convert(RecipeDto recipeDto) {
        if (recipeDto == null || recipeDto.getIngredients() == null) {
            return null;
        }
        RecipeEntity recipeEntity = modelMapper.map(recipeDto, RecipeEntity.class);
        for (IngredientEntity ingredientEntity : recipeEntity.getIngredients()) {
            ingredientEntity.setRecipeEntity(recipeEntity);
        }
        return recipeEntity;
    }

    public RecipeResponse convert(RecipeEntity recipeEntity) {
        if (recipeEntity == null || recipeEntity.getIngredients() == null) {
            return null;
        }
        RecipeResponse recipeResponse = modelMapper.map(recipeEntity, RecipeResponse.class);
        recipeResponse.setIngredients(recipeEntity.getIngredients().stream().map(IngredientEntity::getTitle).collect(Collectors.toList()));
        return recipeResponse;
    }

    public ResponseEntity<MyApiResponse> createResponse(Object recipeResponse, HttpStatus httpStatus) {
        if (recipeResponse == null || httpStatus == null) {
            return null;
        }
        MyApiResponse apiResponse = new MyApiResponse();
        apiResponse.setAction(true);
        apiResponse.setMessage("");
        apiResponse.setDate(new Date());
        apiResponse.setResult(recipeResponse);
        return new ResponseEntity<>(apiResponse, httpStatus);
    }

    public RecipeEntity update(RecipeEntity existedRecipeEntity, RecipeRequest recipeRequest) {
        if (existedRecipeEntity != null && isValidRequestModel(recipeRequest)) {
            existedRecipeEntity.setTitle(recipeRequest.getTitle());
            existedRecipeEntity.setInstructions(recipeRequest.getInstructions());
            existedRecipeEntity.setConsumers(recipeRequest.getConsumers());
            existedRecipeEntity.setVegetarian(recipeRequest.isVegetarian());
            // what do you want to update? ingredients? ...
            return existedRecipeEntity;
        }
        return null;
    }

    public List<RecipeResponse> convert(List<RecipeEntity> recipeEntities) {
        if (recipeEntities == null) {
            return Collections.emptyList();
        }
        return recipeEntities.stream().map(this::convert).collect(Collectors.toList());
    }
}
