package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryCommandToCategoryTest {

  public static final Long ID_VALUE = 1L;
  public static final String DESCRIPTION = "description";

  CategoryCommandToCategory converter;

  @BeforeEach
  void setUp() {
    converter = new CategoryCommandToCategory();
  }

  @Test
  void testNullObject() throws Exception {
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception {
    assertNotNull(converter.convert(new CategoryCommand()));
  }

  @Test
  void convert() throws Exception {

    CategoryCommand categoryCommand = new CategoryCommand();
    categoryCommand.setId(ID_VALUE);
    categoryCommand.setDescription(DESCRIPTION);

    Category category = converter.convert(categoryCommand);

    assertEquals(ID_VALUE,category.getId());
    assertEquals(DESCRIPTION,category.getDescription());

  }
}
