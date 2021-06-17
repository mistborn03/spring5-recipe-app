package guru.springframework.converters;

import guru.springframework.commands.NotesCommand;
import guru.springframework.domain.Notes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NotesCommandToNotesTest {

  public static final Long ID_VALUE = 1L;
  public static final String RECIPE_NOTES = "Notes";

  NotesCommandToNotes converter;

  @BeforeEach
  void setUp() {
    converter = new NotesCommandToNotes();
  }

  @Test
  void testNullParameter() throws Exception {
    assertNull(converter.convert(null));
  }

  @Test
  void testEmptyObject() throws Exception {
    assertNotNull(converter.convert(new NotesCommand()));
  }

  @Test
  void convert() throws Exception {
    NotesCommand notesCommand = new NotesCommand();
    notesCommand.setId(ID_VALUE);
    notesCommand.setRecipeNotes(RECIPE_NOTES);

    Notes notes = converter.convert(notesCommand);

    assertEquals(ID_VALUE, notes.getId());
    assertEquals(RECIPE_NOTES, notes.getRecipeNotes());
  }
}
