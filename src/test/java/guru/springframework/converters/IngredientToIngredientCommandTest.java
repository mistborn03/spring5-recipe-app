package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.domain.Ingredient;
import guru.springframework.domain.Recipe;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientToIngredientCommandTest {

  public static final Recipe RECIPE = new Recipe();
  public static final BigDecimal AMOUNT = new BigDecimal("1");
  public static final String DESCRIPTION = "Cheeseburger";
  public static final Long ID_VALUE = 1L;
  public static final Long UOM_ID = 2L;

  IngredientToIngredientCommand converter;
  
  @BeforeEach
  void setUp() throws Exception{
    converter = new IngredientToIngredientCommand(new UnitOfMeasureToUnitOfMeasureCommand());
  }

  @Test
  void testNullObject() throws Exception {
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception{
    assertNotNull(converter.convert(new Ingredient()));
  }

  @Test
  void convert() throws Exception{
    Ingredient ingredient = new Ingredient();
    ingredient.setId(ID_VALUE);
    ingredient.setDescription(DESCRIPTION);
    ingredient.setRecipe(RECIPE);
    UnitOfMeasure uom = new UnitOfMeasure();
    uom.setId(UOM_ID);
    ingredient.setUom(uom);
    ingredient.setAmount(AMOUNT);

    IngredientCommand command = converter.convert(ingredient);

    assertNotNull(command);
    assertNotNull(command.getUnitOfMeasure());
    assertEquals(ID_VALUE,command.getId());
    assertEquals(DESCRIPTION,command.getDescription());
    assertEquals(AMOUNT,command.getAmount());
    assertEquals(UOM_ID,command.getUnitOfMeasure().getId());

  }

  @Test
  void convertWithNullUOM() throws Exception {
    Ingredient ingredient = new Ingredient();
    ingredient.setId(ID_VALUE);
    ingredient.setDescription(DESCRIPTION);
    ingredient.setRecipe(RECIPE);
    ingredient.setAmount(AMOUNT);

    IngredientCommand command = converter.convert(ingredient);

    assertNotNull(command);
    assertNull(command.getUnitOfMeasure());
    assertEquals(ID_VALUE,command.getId());
    assertEquals(DESCRIPTION,command.getDescription());
    assertEquals(AMOUNT,command.getAmount());

  }
}