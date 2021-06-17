package guru.springframework.converters;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.Ingredient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class IngredientCommandToIngredientTest {

  public static final BigDecimal AMOUNT = new BigDecimal("1");
  public static final String DESCRIPTION = "Cheeseburger";
  public static final Long ID_VALUE = 1L;
  public static final Long UOM_ID = 2L;

  IngredientCommandToIngredient converter;


  @BeforeEach
  void setUp() throws Exception{
    converter = new IngredientCommandToIngredient(new UnitOfMeasureCommandToUnitOfMeasure());
  }

  @Test
  void testNullObject() throws Exception {
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception{
    assertNotNull(converter.convert(new IngredientCommand()));
  }

  @Test
  void convert() throws Exception{
    IngredientCommand command = new IngredientCommand();
    command.setId(ID_VALUE);
    command.setDescription(DESCRIPTION);

    UnitOfMeasureCommand uomc = new UnitOfMeasureCommand();
    uomc.setId(UOM_ID);
    command.setUnitOfMeasure(uomc);
    command.setAmount(AMOUNT);

    Ingredient ingredient = converter.convert(command);

    assertNotNull(ingredient);
    assertNotNull(ingredient.getUom());
    assertEquals(ID_VALUE,ingredient.getId());
    assertEquals(DESCRIPTION,ingredient.getDescription());
    assertEquals(AMOUNT,ingredient.getAmount());
    assertEquals(UOM_ID,ingredient.getUom().getId());

  }

  @Test
  void convertWithNullUOM() throws Exception {
    IngredientCommand command = new IngredientCommand();
    command.setId(ID_VALUE);
    command.setDescription(DESCRIPTION);
    command.setAmount(AMOUNT);

    Ingredient ingredient = converter.convert(command);

    assertNotNull(ingredient);
    assertNull(ingredient.getUom());
    assertEquals(ID_VALUE,ingredient.getId());
    assertEquals(DESCRIPTION,ingredient.getDescription());
    assertEquals(AMOUNT,ingredient.getAmount());

  }
}
