package ir.jimsa.LovelyRecipes.recipes.util;

import ir.jimsa.LovelyRecipes.recipes.model.dto.RecipeDto;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.model.response.RecipeResponse;
import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import ir.jimsa.LovelyRecipes.shared.Utils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        // All check and validation systems ...
        return flag;
    }

    public RecipeEntity convert(RecipeDto recipeDto) {
        return modelMapper.map(recipeDto, RecipeEntity.class);
    }

    public RecipeResponse convert(RecipeEntity recipeEntity) {
        return modelMapper.map(recipeEntity, RecipeResponse.class);
    }

    public ResponseEntity<MyApiResponse> createResponse(RecipeResponse recipeResponse, HttpStatus httpStatus) {
        MyApiResponse apiResponse = new MyApiResponse();
        apiResponse.setAction(true);
        apiResponse.setMessage("");
        apiResponse.setDate(new Date());
        apiResponse.setResult(recipeResponse);
        return new ResponseEntity<>(apiResponse, httpStatus);
    }
}
