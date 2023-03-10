package ir.jimsa.LovelyRecipes.recipes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class IngredientDto implements Serializable {
    private long id;
    private String publicId;
    private String title;
    private RecipeDto recipeDto;
}
