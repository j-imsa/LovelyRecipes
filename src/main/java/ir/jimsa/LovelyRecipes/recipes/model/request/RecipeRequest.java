package ir.jimsa.LovelyRecipes.recipes.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Schema(name = "Recipe", description = "Request Recipe Model")
public class RecipeRequest {
    @Schema(description = "The name of recipe", example = "Ghormeh Sabzi")
    private String title;
    @Schema(description = "All ingredients that made this recipe", example = "[\"Apple\", \"Milk\", \"Sugar\"]")
    private List<String> ingredients;
    @Schema(description = "How we can made it? steps, info and tutorials", example = "First, boil the water, then add some milk ...")
    private String instructions;
    @Schema(description = "It means this recipe works or is enough for how many people?", example = "4")
    private int consumers;
    @Schema(description = "Is this food good for vegetarians? true means Yes, It is good for them and No means Not at all", example = "true")
    private boolean vegetarian;
}
