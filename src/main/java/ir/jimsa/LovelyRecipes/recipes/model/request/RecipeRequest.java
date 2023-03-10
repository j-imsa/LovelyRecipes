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
    private String title;
    private List<String> ingredients;
    private String instructions;
    private int consumers;
    private boolean vegetarian;
}
