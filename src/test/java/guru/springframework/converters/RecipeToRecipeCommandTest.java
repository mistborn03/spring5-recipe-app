package guru.springframework.converters;

import guru.springframework.commands.RecipeCommand;
import guru.springframework.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RecipeToRecipeCommandTest {

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


  RecipeToRecipeCommand converter;

  @BeforeEach
  void setUp() {
    converter = new RecipeToRecipeCommand(new CategoryToCategoryCommand(),new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand()),
            new NotesToNotesCommand());
  }

  @Test
  void testNullObject() throws Exception{
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception {
    assertNotNull(converter.convert(new Recipe()));
  }

  @Test
  void convert() {

    Recipe recipe = new Recipe();
    recipe.setCookTime(COOK_TIME);
    recipe.setPrepTime(PREP_TIME);
    recipe.setDescription(DESCRIPTION);
    recipe.setDirections(DIRECTIONS);
    recipe.setDifficulty(DIFFICULTY);
    recipe.setServings(SERVINGS);
    recipe.setSource(SOURCE);
    recipe.setUrl(URL);
    recipe.setId(RECIPE_ID);

    Notes notes = new Notes();
    notes.setId(NOTES_ID);

    recipe.setNotes(notes);

    Category category = new Category();
    category.setId(CAT_ID_1);

    Category category2 = new Category();
    category.setId(CAT_ID_2);

    recipe.getCategories().add(category);
    recipe.getCategories().add(category2);

    Ingredient ingredient = new Ingredient();
    ingredient.setId(INGRED_ID_1);

    Ingredient ingredient2 = new Ingredient();
    ingredient.setId(INGRED_ID_2);

    recipe.getIngredients().add(ingredient);
    recipe.getIngredients().add(ingredient2);

    RecipeCommand command = converter.convert(recipe);

    assertNotNull(command);
    assertEquals(RECIPE_ID,command.getId());
    assertEquals(COOK_TIME,command.getCookTime());
    assertEquals(PREP_TIME,command.getPrepTime());
    assertEquals(SOURCE,command.getSource());
    assertEquals(URL,command.getUrl());
    assertEquals(DIFFICULTY,command.getDifficulty());
    assertEquals(DIRECTIONS,command.getDirections());
    assertEquals(SERVINGS,command.getServings());
    assertEquals(DESCRIPTION,command.getDescription());
    assertEquals(NOTES_ID,command.getNotes().getId());
    assertEquals(2,command.getCategories().size());
    assertEquals(2,command.getIngredients().size());

  }

}