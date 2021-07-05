package guru.springframework.commands;

import guru.springframework.domain.Difficulty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class RecipeCommand {

  private Long id;

  @NotBlank private String description;

  @Min(1)
  @Max(99)
  private Integer prepTime;

  @Min(1)
  @Max(99)
  private Integer cookTime;

  @Min(1)
  @Max(100)
  private Integer servings;

  private String source;

  @URL
  private String url;

  @NotBlank
  private String directions;

  private Difficulty difficulty;
  private NotesCommand notes;
  private Byte[] image;
  private Set<IngredientCommand> ingredients = new HashSet<>();
  private Set<CategoryCommand> categories = new HashSet<>();
}
