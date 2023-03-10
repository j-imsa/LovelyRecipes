package ir.jimsa.LovelyRecipes.recipes.repository.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "ingredients")
public class IngredientEntity implements Serializable {
    @Id
    @GeneratedValue
    private long id;
    @Column(unique = true, nullable = false)
    private String publicId;
    @Column(nullable = false)
    private String title;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "recipe_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private RecipeEntity recipeEntity;
}
