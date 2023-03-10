package ir.jimsa.LovelyRecipes.recipes.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import ir.jimsa.LovelyRecipes.recipes.model.request.RecipeRequest;
import ir.jimsa.LovelyRecipes.recipes.service.RecipeService;
import ir.jimsa.LovelyRecipes.shared.MyApiResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
@Tag(name = "Recipes", description = "Recipes endpoints")
public class RecipeController {
    Logger logger = LoggerFactory.getLogger(RecipeController.class);
    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Adding a new recipe into database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Return a stored recipe with a generated publicId",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "When the input be an invalid, or unprocessable",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "When the recipe already exist",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "System Default Exception (SDE), or when database IO exception occurred",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
    })
    public ResponseEntity<MyApiResponse> createNewRecipe(@RequestBody RecipeRequest recipeRequest) {
        logger.info("Retrieve create new recipe method for title: {}", recipeRequest.getTitle());
        return recipeService.createNewRecipe(recipeRequest);
    }

    @DeleteMapping(
            path = "/{publicId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Removing a recipe from database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully has deleted",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "When could not found the recipe",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "System Default Exception (SDE)",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
    })
    public ResponseEntity<MyApiResponse> deleteRecipe(@PathVariable String publicId) {
        logger.info("Retrieve delete recipe by pId: {}", publicId);
        return recipeService.deleteRecipe(publicId);
    }

    @PutMapping(
            path = "/{publicId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Updating the information of a recipe on database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successfully has updated",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "When could not found the recipe",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "422",
                    description = "When the input be an invalid, or unprocessable",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "System Default Exception (SDE)",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
    })
    public ResponseEntity<MyApiResponse> editRecipe(
            @PathVariable String publicId,
            @RequestBody RecipeRequest recipeRequest) {
        logger.info("Retrieve edit recipe by pId: {}", publicId);
        return recipeService.editRecipe(publicId, recipeRequest);
    }

    @GetMapping(
            path = "/v2",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Getting recipes from database using pagination")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return all stored recipes",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "System Default Exception (SDE)",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
    })
    public ResponseEntity<MyApiResponse> getRecipes(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "25") int size
    ) {
        logger.info("Retrieve all recipes by page: {} and limit: {}", page, size);
        return recipeService.getRecipes(page, size);
    }

    @GetMapping(
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
    )
    @Operation(summary = "Searching recipes from database")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Return all stored recipes",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "System Default Exception (SDE)",
                    content = {@Content(mediaType = "application/json"), @Content(mediaType = "application/xml")}
            ),
    })
    public ResponseEntity<MyApiResponse> getRecipes(
            @RequestParam(value = "consumers", defaultValue = "-1") int consumers,
            @RequestParam(value = "vegetarian", defaultValue = "false") boolean vegetarian,
            @RequestParam(value = "include", defaultValue = "") String include,
            @RequestParam(value = "exclude", defaultValue = "") String exclude,
            @RequestParam(value = "instructions", defaultValue = "") String instructions
    ) {
        logger.info("Retrieve all recipe");
        return recipeService.searchRecipes(consumers, vegetarian, include, exclude, instructions);
    }
}
