package guru.springframework.service;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.converters.RecipeCommandToRecipe;
import guru.springframework.converters.RecipeToRecipeCommand;
import guru.springframework.domain.Recipe;
import guru.springframework.repositories.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

  RecipeServiceImpl recipeService;

  @Mock RecipeRepository recipeRepository;

  @Mock
  RecipeToRecipeCommand recipeToRecipeCommand;

  @Mock
  RecipeCommandToRecipe recipeCommandToRecipe;

  @BeforeEach
  void setUp() {
//    MockitoAnnotations.initMocks(this);
    recipeService = new RecipeServiceImpl(recipeRepository,recipeToRecipeCommand,recipeCommandToRecipe);
  }

  @Test
  public void getRecipeByIdTest() throws Exception {
    Recipe recipe = new Recipe();
    recipe.setId(1L);
    Optional<Recipe> recipeOptional = Optional.of(recipe);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

    Recipe recipeReturned = recipeService.findById(1L);

    assertNotNull(recipeReturned, "recipe not returned");
    verify(recipeRepository, times(1)).findById(anyLong());
    verify(recipeRepository, never()).findAll();
  }

  @Test
  void getRecipes() throws Exception {
    Recipe recipe = new Recipe();
    HashSet recipesData = new HashSet();
    recipesData.add(recipe);

    when(recipeRepository.findAll()).thenReturn(recipesData);

    Set<Recipe> recipes = recipeService.getRecipes();
    assertEquals(recipes.size(), 1);
    verify(recipeRepository, times(1)).findAll();
    verify(recipeRepository, never()).findById(anyLong());
  }


  @Test
  void getRecipeCommandByIdTest() throws Exception {
    Recipe recipe =new Recipe();
    recipe.setId(1L);
    Optional<Recipe> recipeOptional = Optional.of(recipe);

    when(recipeRepository.findById(anyLong())).thenReturn(recipeOptional);

    RecipeCommand recipeCommand = new RecipeCommand();
    recipeCommand.setId(1L);

    when(recipeToRecipeCommand.convert(any())).thenReturn(recipeCommand);

    RecipeCommand commandById = recipeService.findCommandById(1L);

    assertNotNull(commandById,"null recipe returned");
    verify(recipeRepository,times(1)).findById(anyLong());
    verify(recipeRepository,never()).findAll();

  }

}
