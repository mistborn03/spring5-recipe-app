package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.NotesCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.Difficulty;
import guru.springframework.domain.Recipe;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeCommandToRecipeTest {


  public static final Integer COOK_TIME = Integer.valueOf("5");
  public static final Integer PREP_TIME = Integer.valueOf("7");;
  public static final String DESCRIPTION = "My Recipe";
  public static final String DIRECTIONS = "Directions";
  public static final Integer SERVINGS = Integer.valueOf("3");
  public static final String SOURCE = "Source";
  public static final String URL = "Some URL";
  public static final long ID = 1L;
  public static final long INGRED_ID_1 = ID;
  public static final long RECIPE_ID = 1L;
  public static final Difficulty DIFFICULTY = Difficulty.EASY;
  public static final long NOTES_ID = 9L;
  public static final long CAT_ID_1 = 1L;
  public static final long CAT_ID_2 = 2L;
  public static final long INGRED_ID_2 = 2L;


  RecipeCommandToRecipe converter;

  @BeforeEach
  void setUp() {
    converter = new RecipeCommandToRecipe(new CategoryCommandToCategory(), new NotesCommandToNotes(),
            new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure()));
  }

  @Test
  void testNullObject() throws Exception{
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception {
    assertNotNull(converter.convert(new RecipeCommand()));
  }

  @Test
  void convert() {

    RecipeCommand recipeCommand = new RecipeCommand();
    recipeCommand.setCookTime(COOK_TIME);
    recipeCommand.setPrepTime(PREP_TIME);
    recipeCommand.setDescription(DESCRIPTION);
    recipeCommand.setDirections(DIRECTIONS);
    recipeCommand.setDifficulty(DIFFICULTY);
    recipeCommand.setServings(SERVINGS);
    recipeCommand.setSource(SOURCE);
    recipeCommand.setUrl(URL);
    recipeCommand.setId(RECIPE_ID);

    NotesCommand notes = new NotesCommand();
    notes.setId(NOTES_ID);

    recipeCommand.setNotes(notes);

    CategoryCommand category = new CategoryCommand();
    category.setId(CAT_ID_1);

    CategoryCommand category2 = new CategoryCommand();
    category.setId(CAT_ID_2);

    recipeCommand.getCategories().add(category);
    recipeCommand.getCategories().add(category2);

    IngredientCommand ingredient = new IngredientCommand();
    ingredient.setId(INGRED_ID_1);

    IngredientCommand ingredient2 = new IngredientCommand();
    ingredient.setId(INGRED_ID_2);

    recipeCommand.getIngredients().add(ingredient);
    recipeCommand.getIngredients().add(ingredient2);

    Recipe recipe = converter.convert(recipeCommand);

    assertNotNull(recipe);
    assertEquals(RECIPE_ID,recipe.getId());
    assertEquals(COOK_TIME,recipe.getCookTime());
    assertEquals(PREP_TIME,recipe.getPrepTime());
    assertEquals(SOURCE,recipe.getSource());
    assertEquals(URL,recipe.getUrl());
    assertEquals(DIFFICULTY,recipe.getDifficulty());
    assertEquals(DIRECTIONS,recipe.getDirections());
    assertEquals(SERVINGS,recipe.getServings());
    assertEquals(DESCRIPTION,recipe.getDescription());
    assertEquals(NOTES_ID,recipe.getNotes().getId());
    assertEquals(2,recipe.getCategories().size());
    assertEquals(2,recipe.getIngredients().size());

  }

}
