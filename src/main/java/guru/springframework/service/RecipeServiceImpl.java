package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class RecipeServiceImpl implements RecipeService {

  private final RecipeRepository recipeRepository;
  private final RecipeToRecipeCommand recipeToRecipeCommand;
  private final RecipeCommandToRecipe recipeCommandToRecipe;

  public RecipeServiceImpl(
      RecipeRepository recipeRepository,
      RecipeToRecipeCommand recipeToRecipeCommand,
      RecipeCommandToRecipe recipeCommandToRecipe) {
    this.recipeRepository = recipeRepository;
    this.recipeToRecipeCommand = recipeToRecipeCommand;
    this.recipeCommandToRecipe = recipeCommandToRecipe;
  }

  @Override
  public Set<Recipe> getRecipes() {
    log.debug("I'm in the service");

    Set<Recipe> recipeSet = new HashSet<>();
    recipeRepository.findAll().iterator().forEachRemaining(recipeSet::add);
    return recipeSet;
  }

  @Override
  public Recipe findById(Long l) {

    Optional<Recipe> recipeOptional = recipeRepository.findById(l);

    if (!recipeOptional.isPresent()) {
      throw new RuntimeException("recipe not found");
    }

    return recipeOptional.get();
  }

  @Transactional
  @Override
  public RecipeCommand findCommandById(Long l) {
    return recipeToRecipeCommand.convert(findById(l));
  }

  @Transactional
  @Override
  public RecipeCommand saveRecipeCommand(RecipeCommand command) {
    Recipe detachedRecipe = recipeCommandToRecipe.convert(command);

    Recipe savedRecipe = recipeRepository.save(detachedRecipe);
    log.debug("Saved recipe Id:" + savedRecipe.getId());
    return recipeToRecipeCommand.convert(savedRecipe);
  }
}
