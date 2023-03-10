package ir.jimsa.LovelyRecipes.recipes.repository;

import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface RecipeRepository extends PagingAndSortingRepository<RecipeEntity, Long> {
    RecipeEntity findByTitle(String title);
}
