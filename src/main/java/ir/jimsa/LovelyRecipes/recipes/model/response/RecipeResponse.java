package ir.jimsa.LovelyRecipes.recipes.model.response;

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
@Schema(name = "Recipe", description = "Response Recipe Model")
public class RecipeResponse {
    private String publicId;
    private String title;
    private List<String> ingredients;
    private String instructions;
    private int consumers;
}
