package guru.springframework.service;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.converters.IngredientToIngredientCommand;
import guru.springframework.converters.UnitOfMeasureToUnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
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

  @Mock RecipeRepository recipeRepository;

  IngredientService ingredientService;

  public IngredientServiceImplTest() {
    this.ingredientToIngredientCommand =
        new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
  }

  @BeforeEach
  void setUp() {
    ingredientService = new IngredientServiceImpl(ingredientToIngredientCommand, recipeRepository);
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

    IngredientCommand command = ingredientService.findByRecipeIdAndIngredientId(1L,3L);

    assertEquals(Long.valueOf(3L),command.getId());
    assertEquals(Long.valueOf(1L),command.getRecipeId());
    verify(recipeRepository,times(1)).findById(anyLong());
  }
}
