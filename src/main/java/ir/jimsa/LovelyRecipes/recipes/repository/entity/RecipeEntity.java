package ir.jimsa.LovelyRecipes.recipes.repository.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "recipe")
public class RecipeEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true, nullable = false)
    private String publicId;
    private String title;
    private String instructions;
    private int consumers;
    private boolean vegetarian;
    @OneToMany(
            mappedBy = "recipeEntity",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<IngredientEntity> ingredients;
}
