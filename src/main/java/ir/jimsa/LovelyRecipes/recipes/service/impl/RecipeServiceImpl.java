package ir.jimsa.LovelyRecipes.recipes.service.impl;

import ir.jimsa.LovelyRecipes.config.exception.SystemServiceException;
import ir.jimsa.LovelyRecipes.recipes.model.dto.RecipeDto;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.repository.RecipeRepository;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import ir.jimsa.LovelyRecipes.recipes.service.RecipeService;
import ir.jimsa.LovelyRecipes.recipes.util.RecipeUtil;
import ir.jimsa.LovelyRecipes.shared.Constants;
import ir.jimsa.LovelyRecipes.shared.ErrorMessages;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @Override
    public ResponseEntity<MyApiResponse> deleteRecipe(String publicId) {
        RecipeEntity existedRecipeEntity = recipeRepository.findByPublicId(publicId);
        if (existedRecipeEntity == null) {
            throw new SystemServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), HttpStatus.NOT_FOUND);
        }
        try {
            recipeRepository.delete(existedRecipeEntity);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw new SystemServiceException(ErrorMessages.DATABASE_IO_EXCEPTION.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return recipeUtil.createResponse(new ArrayList<>(), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MyApiResponse> editRecipe(String publicId, RecipeRequest recipeRequest) {
        RecipeEntity existedRecipeEntity = recipeRepository.findByPublicId(publicId);
        if (existedRecipeEntity == null) {
            throw new SystemServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage(), HttpStatus.NOT_FOUND);
        }
        RecipeEntity updateRecipeEntity;
        updateRecipeEntity = recipeUtil.update(existedRecipeEntity, recipeRequest);
        if (updateRecipeEntity == null) {
            throw new SystemServiceException(ErrorMessages.INVALID_INPUT_VALUE.getErrorMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            updateRecipeEntity = recipeRepository.save(updateRecipeEntity);
        } catch (Exception exception) {
            logger.error(exception.getMessage());
            throw new SystemServiceException(ErrorMessages.DATABASE_IO_EXCEPTION.getErrorMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return recipeUtil.createResponse(recipeUtil.convert(updateRecipeEntity), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MyApiResponse> getRecipes(int page, int size) {
        if (page > 0) page--;
        Pageable pageable = PageRequest.of(page, size);
        Page<RecipeEntity> recipeEntityPage = recipeRepository.findAll(pageable);
        List<RecipeEntity> recipeEntities = recipeEntityPage.getContent();
        return recipeUtil.createResponse(recipeUtil.convert(recipeEntities), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<MyApiResponse> searchRecipes(int consumers, boolean vegetarian, String include, String exclude, String instructions) {
        // An idea: getting data from db (50%) and work on it (50%)

        // db: consumers and vegetarian
        List<RecipeEntity> recipeEntities;
        if (vegetarian) {
            recipeEntities = recipeRepository.findAllByConsumersAndVegetarian(consumers, true);
        } else if (consumers == Constants.INVALID) {
            recipeEntities = (List<RecipeEntity>) recipeRepository.findAll();
        } else {
            recipeEntities = recipeRepository.findAllByConsumers(consumers);
        }

        // server: include and exclude from ingredients, and contain an instruction in instructions
        if (!include.isEmpty()) {
            recipeEntities = recipeEntities.stream()
                    .filter(recipeEntity ->
                            recipeEntity.getIngredients().stream().anyMatch(ingredientEntity -> ingredientEntity.getTitle().equals(include)))
                    .collect(Collectors.toList());
        }
        if (!exclude.isEmpty()) {
            recipeEntities = recipeEntities.stream()
                    .filter(recipeEntity ->
                            recipeEntity.getIngredients().stream().noneMatch(ingredientEntity -> ingredientEntity.getTitle().equals(exclude)))
                    .collect(Collectors.toList());
        }
        if (!instructions.isEmpty()) {
            recipeEntities = recipeEntities.stream()
                    .filter(recipeEntity ->
                            recipeEntity.getInstructions().contains(instructions))
                    .collect(Collectors.toList());
        }

        return recipeUtil.createResponse(recipeUtil.convert(recipeEntities), HttpStatus.OK);
    }


}

