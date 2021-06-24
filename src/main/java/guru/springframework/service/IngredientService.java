package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;

public interface IngredientService {
    IngredientCommand findByRecipeIdAndIngredientId(Long recipeId,Long ingredientId);
    IngredientCommand saveIngredientCommand(IngredientCommand command);
    IngredientCommand deleteById(Long recipeId,Long ingredientId);
}
