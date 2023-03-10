package ir.jimsa.LovelyRecipes.recipes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class RecipeDto implements Serializable {

    private long id;
    private String publicId;
    private String title;
    private List<IngredientDto> ingredients;
    private String instructions;
    private int consumers;
}
