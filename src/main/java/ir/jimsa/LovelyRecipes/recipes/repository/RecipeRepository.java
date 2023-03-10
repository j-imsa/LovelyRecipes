package ir.jimsa.LovelyRecipes.recipes.repository;

import ir.jimsa.LovelyRecipes.recipes.repository.entity.RecipeEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface RecipeRepository extends PagingAndSortingRepository<RecipeEntity, Long> {
    RecipeEntity findByTitle(String title);

    RecipeEntity findByPublicId(String publicId);

    List<RecipeEntity> findAllByConsumersAndVegetarian(int consumers, boolean isVegetarian);

    List<RecipeEntity> findAllByConsumers(int consumers);
}
