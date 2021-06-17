package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesToNotesCommandTest {

  public static final Long ID_VALUE = 1L;
  public static final String RECIPE_NOTES = "Notes";

  NotesToNotesCommand converter;

  @BeforeEach
  void setUp() {
    converter = new NotesToNotesCommand();
  }

  @Test
  void testNullParameter() throws Exception {
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception {
    assertNotNull(converter.convert(new Notes()));
  }

  @Test
  void convert() throws Exception {
    Notes notes = new Notes();
    notes.setId(ID_VALUE);
    notes.setRecipeNotes(RECIPE_NOTES);

    NotesCommand notesCommand = converter.convert(notes);

    assertEquals(ID_VALUE, notesCommand.getId());
    assertEquals(RECIPE_NOTES, notesCommand.getRecipeNotes());
  }
}