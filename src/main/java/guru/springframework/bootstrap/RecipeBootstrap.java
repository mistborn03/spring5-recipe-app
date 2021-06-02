package guru.springframework.bootstrap;

import guru.springframework.domain.*;
import guru.springframework.repositories.CategoryRepository;
import guru.springframework.repositories.RecipeRepository;
import guru.springframework.repositories.UnitOfMeasureRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class RecipeBootstrap implements ApplicationListener<ContextRefreshedEvent> {

  private CategoryRepository categoryRepository;
  private RecipeRepository recipeRepository;
  private UnitOfMeasureRepository unitOfMeasureRepository;

  public RecipeBootstrap(
      CategoryRepository categoryRepository,
      RecipeRepository recipeRepository,
      UnitOfMeasureRepository unitOfMeasureRepository) {
    this.categoryRepository = categoryRepository;
    this.recipeRepository = recipeRepository;
    this.unitOfMeasureRepository = unitOfMeasureRepository;
  }

  @Override
  @Transactional
  public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
    recipeRepository.saveAll(getRecipes());
    log.debug("Loading bootstrap data");
  }

  private List<Recipe> getRecipes() {

    List<Recipe> recipes = new ArrayList<>(2);

    Optional<UnitOfMeasure> eachUomOptional = unitOfMeasureRepository.findByDescription("Each");
    if (!eachUomOptional.isPresent()) {
      throw new RuntimeException("expected UOM not found");
    }

    Optional<UnitOfMeasure> tableSpoonUomOptional =
        unitOfMeasureRepository.findByDescription("Tablespoon");
    if (!tableSpoonUomOptional.isPresent()) {
      throw new RuntimeException("expected UOM not found");
    }

    Optional<UnitOfMeasure> teaSpoonUomOptional =
        unitOfMeasureRepository.findByDescription("Teaspoon");
    if (!teaSpoonUomOptional.isPresent()) {
      throw new RuntimeException("expected UOM not found");
    }

    Optional<UnitOfMeasure> dashUomOptional = unitOfMeasureRepository.findByDescription("Dash");

    if (!dashUomOptional.isPresent()) {
      throw new RuntimeException("Expected UOM Not Found");
    }

    Optional<UnitOfMeasure> pintUomOptional = unitOfMeasureRepository.findByDescription("Pint");

    if (!pintUomOptional.isPresent()) {
      throw new RuntimeException("Expected UOM Not Found");
    }

    Optional<UnitOfMeasure> cupUomOptional = unitOfMeasureRepository.findByDescription("Cup");

    if (!cupUomOptional.isPresent()) {
      throw new RuntimeException("Expected UOM Not Found");
    }

    UnitOfMeasure eachUOM = eachUomOptional.get();
    UnitOfMeasure tableSpoonUom = tableSpoonUomOptional.get();
    UnitOfMeasure teaspoonUom = teaSpoonUomOptional.get();
    UnitOfMeasure dashUom = dashUomOptional.get();
    UnitOfMeasure pintUom = pintUomOptional.get();
    UnitOfMeasure cupsUom = cupUomOptional.get();

    // get Categories
    Optional<Category> americanCategoryOptional = categoryRepository.findByDescription("American");

    if (!americanCategoryOptional.isPresent()) {
      throw new RuntimeException("Expected Category Not Found");
    }

    Optional<Category> mexicanCategoryOptional = categoryRepository.findByDescription("Mexican");

    if (!mexicanCategoryOptional.isPresent()) {
      throw new RuntimeException("Expected Category Not Found");
    }

    Category americanCategory = americanCategoryOptional.get();
    Category mexicanCategory = mexicanCategoryOptional.get();

    Recipe guacRecipe = new Recipe();
    guacRecipe.setDescription("Perfect Guacamole");
    guacRecipe.setPrepTime(10);
    guacRecipe.setCookTime(0);
    guacRecipe.setDifficulty(Difficulty.EASY);
    guacRecipe.setDirections("1. aaa" +
            "\n" +
            "2. adada");


      Notes guacNotes = new Notes();
      guacNotes.setRecipeNotes("For a very quick guacamole just take a 1/4 cup of salsa and mix it in with your mashed avocados.\n");

      guacRecipe.setNotes(guacNotes);

      guacRecipe.addIngredient(new Ingredient("ripe Avocados",new BigDecimal(2),eachUOM));
      guacRecipe.addIngredient(new Ingredient("Kosher salt", new BigDecimal(".5"), teaspoonUom));
      guacRecipe.addIngredient(new Ingredient("fresh lime juice or lemon juice", new BigDecimal(2), tableSpoonUom));
      guacRecipe.addIngredient(new Ingredient("minced red onion or thinly sliced green onion", new BigDecimal(2), tableSpoonUom));
      guacRecipe.addIngredient(new Ingredient("serrano chiles, stems and seeds removed, minced", new BigDecimal(2), eachUOM));
      guacRecipe.addIngredient(new Ingredient("Cilantro", new BigDecimal(2), tableSpoonUom));
      guacRecipe.addIngredient(new Ingredient("freshly grated black pepper", new BigDecimal(2), dashUom));
      guacRecipe.addIngredient(new Ingredient("ripe tomato, seeds and pulp removed, chopped", new BigDecimal(".5"), eachUOM));

      guacRecipe.getCategories().add(americanCategory);
      guacRecipe.getCategories().add(mexicanCategory);

      recipes.add(guacRecipe);

      return recipes;
  }
}
