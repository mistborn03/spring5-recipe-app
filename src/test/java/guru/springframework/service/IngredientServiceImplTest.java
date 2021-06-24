package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientCommandToIngredient;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureCommandToUnitOfMeasure;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IngredientServiceImplTest {

  private final IngredientToIngredientCommand ingredientToIngredientCommand;
  private final IngredientCommandToIngredient ingredientCommandToIngredient;

  @Mock RecipeRepository recipeRepository;

  @Mock UnitOfMeasureRepository unitOfMeasureRepository;

  IngredientService ingredientService;

  public IngredientServiceImplTest() {
    this.ingredientToIngredientCommand =
        new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
    this.ingredientCommandToIngredient =
        new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
  }

  @BeforeEach
  void setUp() {
    ingredientService =
        new IngredientServiceImpl(
            ingredientToIngredientCommand,
            recipeRepository,
            ingredientCommandToIngredient,
            unitOfMeasureRepository);
  }

  @Test
  void findByRecipeIdAndIngredientIdHappyPath() {
    Recipe recipe = new Recipe();
    recipe.setId(1L);

    Ingredient ingredient1 = new Ingredient();
    ingredient1.setId(1L);

    Ingredient ingredient2 = new Ingredient();
    ingredient2.setId(1L);

    Ingredient ingredient3 = new Ingredient();
    ingredient3.setId(3L);

    recipe.addIngredient(ingredient1);
    recipe.addIngredient(ingredient2);
    recipe.addIngredient(ingredient3);

    Optional<Recipe> recipeOptional = Optional.of(recipe);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

    IngredientCommand command = ingredientService.findByRecipeIdAndIngredientId(1L, 3L);

    assertEquals(Long.valueOf(3L), command.getId());
    assertEquals(Long.valueOf(1L), command.getRecipeId());
    verify(recipeRepository, times(1)).findById(anyLong());
  }

  @Test
  public void testSaveRecipeCommand() throws Exception {
    IngredientCommand command = new IngredientCommand();
    command.setId(3L);
    command.setRecipeId(1L);

    Optional<Recipe> recipeOptional = Optional.of(new Recipe());

    Recipe savedRecipe = new Recipe();
    savedRecipe.addIngredient(new Ingredient());
    savedRecipe.getIngredients().iterator().next().setId(3L);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);
    when(recipeRepository.save(any())).thenReturn(savedRecipe);

    IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

    assertEquals(3L, savedCommand.getId());
    verify(recipeRepository, times(1)).findById(anyLong());
    verify(recipeRepository, times(1)).save(any(Recipe.class));
  }

  @Test
  public void testDeleteById() throws Exception{
    Recipe recipe = new Recipe();
    Ingredient ingredient = new Ingredient();
    ingredient.setId(3L);
    recipe.addIngredient(ingredient);
    ingredient.setRecipe(recipe);

    Optional<Recipe> recipeOptional = Optional.of(recipe);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

    ingredientService.deleteById(1L,3L);

    verify(recipeRepository,times(1)).findById(anyLong());
    verify(recipeRepository,times(1)).save(any(Recipe.class));

  }
}
