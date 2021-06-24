package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
public class IngredientServiceImpl implements IngredientService {

  private final IngredientToIngredientCommand ingredientToIngredientCommand;
  private final RecipeRepository recipeRepository;
  private final IngredientCommandToIngredient ingredientCommandToIngredient;
  private final UnitOfMeasureRepository unitOfMeasureRepository;

  public IngredientServiceImpl(
      IngredientToIngredientCommand ingredientToIngredientCommand,
      RecipeRepository recipeRepository,
      IngredientCommandToIngredient ingredientCommandToIngredient,
      UnitOfMeasureRepository unitOfMeasureRepository) {
    this.ingredientToIngredientCommand = ingredientToIngredientCommand;
    this.recipeRepository = recipeRepository;
    this.ingredientCommandToIngredient = ingredientCommandToIngredient;
    this.unitOfMeasureRepository = unitOfMeasureRepository;
  }

  @Override
  public IngredientCommand findByRecipeIdAndIngredientId(Long recipeId, Long ingredientId) {

    Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

    if (!recipeOptional.isPresent()) log.error("recipe id is not found :  " + recipeId);

    Recipe recipe = recipeOptional.get();

    Optional<IngredientCommand> ingredientCommandOptional =
        recipe.getIngredients().stream()
            .filter(ingredient -> ingredient.getId().equals(ingredientId))
            .map(ingredient -> ingredientToIngredientCommand.convert(ingredient))
            .findFirst();

    if (!ingredientCommandOptional.isPresent())
      log.error("ingredient id is not found : " + ingredientId);

    return ingredientCommandOptional.get();
  }

  @Transactional
  @Override
  public IngredientCommand saveIngredientCommand(IngredientCommand command) {

    Optional<Recipe> recipeOptional = recipeRepository.findById(command.getRecipeId());

    if (!recipeOptional.isPresent()) {

      log.error("recipe not found for id: " + command.getRecipeId());
      return new IngredientCommand();
    }

    Recipe recipe = recipeOptional.get();
    Optional<Ingredient> ingredientOptional =
        recipe.getIngredients().stream()
            .filter(ingredient -> ingredient.getId().equals(command.getId()))
            .findFirst();

    if (ingredientOptional.isPresent()) {
      Ingredient ingredientFound = ingredientOptional.get();
      ingredientFound.setDescription(command.getDescription());
      ingredientFound.setAmount(command.getAmount());
      ingredientFound.setUom(
          unitOfMeasureRepository
              .findById(command.getUom().getId())
              .orElseThrow(() -> new RuntimeException("UOM not found")));
    } else {
      Ingredient ingredient = ingredientCommandToIngredient.convert(command);
      ingredient.setRecipe(recipe);
      recipe.addIngredient(ingredient);
    }

    Recipe savedRecipe = recipeRepository.save(recipe);

    Optional<Ingredient> savedIngredientOptional =
        savedRecipe.getIngredients().stream()
            .filter(recipeIngredient -> recipeIngredient.getId().equals(command.getId()))
            .findFirst();

    if (!savedIngredientOptional.isPresent()) {

      savedIngredientOptional =
          savedRecipe.getIngredients().stream()
              .filter(
                  recipeIngredient ->
                      recipeIngredient.getDescription().equals(command.getDescription()))
              .filter(recipeIngredient -> recipeIngredient.getAmount().equals(command.getAmount()))
              .filter(
                  recipeIngredient ->
                      recipeIngredient.getUom().getId().equals(command.getUom().getId()))
              .findFirst();
    }

    return ingredientToIngredientCommand.convert(savedIngredientOptional.get());
  }

  @Override
  public IngredientCommand deleteById(Long recipeId, Long ingredientId) {

    log.debug("deleting ingredient: " + recipeId + ":" + ingredientId);

    Optional<Recipe> recipeOptional = recipeRepository.findById(recipeId);

    if (recipeOptional.isPresent()) {

      Recipe recipe = recipeOptional.get();
      log.debug("found recipe");

      Optional<Ingredient> ingredientOptional =
          recipe.getIngredients().stream()
              .filter(ingredient -> ingredient.getId().equals(ingredientId))
              .findFirst();

      if (ingredientOptional.isPresent()) {
        log.debug("ingredient found");
        Ingredient ingredient = ingredientOptional.get();
        ingredient.setRecipe(null);
        recipe.getIngredients().remove(ingredientOptional.get());
        recipeRepository.save(recipe);
      }
    }else {
      log.debug("recipe id not found . Id: " + recipeId);
    }

    return null;
  }
}
