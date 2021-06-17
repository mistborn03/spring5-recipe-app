package guru.springframework.converters;

import guru.springframework.commands.CategoryCommand;
import guru.springframework.domain.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CategoryToCategoryCommandTest {

  public static final Long ID_VALUE = 1L;
  public static final String DESCRIPTION = "description";

  CategoryToCategoryCommand converter;

  @BeforeEach
  void setUp() {
    converter = new CategoryToCategoryCommand();
  }

  @Test
  void testNullObject() throws Exception {
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception {
    assertNotNull(converter.convert(new Category()));
  }

  @Test
  void convert() throws Exception {

    Category category = new Category();
    category.setId(ID_VALUE);
    category.setDescription(DESCRIPTION);

    CategoryCommand categoryCommand = converter.convert(category);

    assertEquals(ID_VALUE, categoryCommand.getId());
    assertEquals(DESCRIPTION, categoryCommand.getDescription());
  }
}
