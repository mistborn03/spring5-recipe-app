package guru.springframework.converters;

import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.domain.UnitOfMeasure;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UnitOfMeasureToUnitOfMeasureCommandTest {

  public static final Long ID_VALUE = 1L;
  public static final String DESCRIPTION = "Notes";

  UnitOfMeasureToUnitOfMeasureCommand converter;

  @BeforeEach
  void setUp() {
    converter = new UnitOfMeasureToUnitOfMeasureCommand();
  }

  @Test
  void testNullParameter() throws Exception {
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception {
    assertNotNull(converter.convert(new UnitOfMeasure()));
  }

  @Test
  void convert() throws Exception {
    UnitOfMeasure uom = new UnitOfMeasure();
    uom.setId(ID_VALUE);
    uom.setDescription(DESCRIPTION);

    UnitOfMeasureCommand command = converter.convert(uom);

    assertEquals(ID_VALUE, command.getId());
    assertEquals(DESCRIPTION, command.getDescription());
  }
}