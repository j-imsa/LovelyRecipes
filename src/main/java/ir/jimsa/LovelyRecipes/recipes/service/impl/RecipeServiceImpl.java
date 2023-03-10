package ir.jimsa.LovelyRecipes.recipes.service.impl;

import ir.jimsa.LovelyRecipes.config.exception.SystemServiceException;
import ir.jimsa.LovelyRecipes.recipes.model.dto.RecipeDto;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.repository.RecipeRepository;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import ir.jimsa.LovelyRecipes.recipes.service.RecipeService;
import ir.jimsa.LovelyRecipes.recipes.util.RecipeUtil;
import ir.jimsa.LovelyRecipes.shared.ErrorMessages;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import ir.jimsa.LovelyRecipes.shared.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RecipeServiceImpl implements RecipeService {
    Logger logger = LoggerFactory.getLogger(RecipeServiceImpl.class);
    private final RecipeUtil recipeUtil;
    private final RecipeRepository recipeRepository;

    public RecipeServiceImpl(RecipeUtil recipeUtil, RecipeRepository recipeRepository) {
        this.recipeUtil = recipeUtil;
        this.recipeRepository = recipeRepository;
    }

    @Override
    public ResponseEntity<MyApiResponse> createNewRecipe(RecipeRequest recipeRequest) {

        RecipeEntity existedRecipeEntity = recipeRepository.findByTitle(recipeRequest.getTitle());
        if (existedRecipeEntity != null) {
            throw new SystemServiceException(ErrorMessages.RECORD_ALREADY_EXISTS.getErrorMessage(), HttpStatus.CONFLICT);
        }

        RecipeDto recipeDto = recipeUtil.convert(recipeRequest);
        if (recipeDto == null) {
            throw new SystemServiceException(ErrorMessages.INVALID_INPUT_VALUE.getErrorMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }

        RecipeEntity recipeEntity = recipeUtil.convert(recipeDto);
        RecipeEntity storedRecipeEntity;
        try {
            storedRecipeEntity = recipeRepository.save(recipeEntity);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw new SystemServiceException(ErrorMessages.DATABASE_IO_EXCEPTION.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return recipeUtil.createResponse(recipeUtil.convert(storedRecipeEntity), HttpStatus.CREATED);
    }
}

