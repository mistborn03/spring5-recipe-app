package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.commands.UnitOfMeasureCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Slf4j
@Controller
public class IngredientController {

  private final RecipeService recipeService;
  private final IngredientService ingredientService;
  private final UnitOfMeasureService unitOfMeasureService;

  public IngredientController(
      RecipeService recipeService,
      IngredientService ingredientService,
      UnitOfMeasureService unitOfMeasureService) {
    this.recipeService = recipeService;
    this.ingredientService = ingredientService;
    this.unitOfMeasureService = unitOfMeasureService;
  }

  @GetMapping("/recipe/{recipeId}/ingredients")
  public String listIngredients(@PathVariable String recipeId, Model model) {

    log.debug("Getting ingredient list for recipe id:  " + recipeId);

    model.addAttribute("recipe", recipeService.findCommandById(Long.valueOf(recipeId)));

    return "recipe/ingredient/list";
  }

  @GetMapping("/recipe/{recipeId}/ingredient/{id}/show")
  public String showRecipeIngredient(
      @PathVariable String recipeId, @PathVariable String id, Model model) {
    model.addAttribute(
        "ingredient",
        ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));
    return "recipe/ingredient/show";
  }

  @GetMapping("/recipe/{recipeId}/ingredient/{id}/update")
  public String updateRecipeIngredient(
      @PathVariable String recipeId, @PathVariable String id, Model model) {
    model.addAttribute(
        "ingredient",
        ingredientService.findByRecipeIdAndIngredientId(Long.valueOf(recipeId), Long.valueOf(id)));

    model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

    return "recipe/ingredient/ingredientform";
  }

  @PostMapping("recipe/{recipeId}/ingredient")
  public String saveOrUpdate(@ModelAttribute IngredientCommand command) {

    IngredientCommand savedCommand = ingredientService.saveIngredientCommand(command);

    log.debug("save recipe id: " + savedCommand.getRecipeId());
    log.debug("save ingredient id: " + savedCommand.getId());

    return "redirect:/recipe/"
        + savedCommand.getRecipeId()
        + "/ingredient/"
        + savedCommand.getId()
        + "/show";
  }

  @GetMapping("recipe/{recipeId}/ingredient/new")
  public String newIngredient(@PathVariable String recipeId, Model model) {

    RecipeCommand recipeCommand = recipeService.findCommandById(Long.valueOf(recipeId));

    IngredientCommand ingredientCommand = new IngredientCommand();
    ingredientCommand.setRecipeId(Long.valueOf(recipeId));
    model.addAttribute("ingredient", ingredientCommand);

    ingredientCommand.setUom(new UnitOfMeasureCommand());

    model.addAttribute("uomList", unitOfMeasureService.listAllUoms());

    return "recipe/ingredient/ingredientform";
  }

  @GetMapping("/recipe/{recipeId}/ingredient/{id}/delete")
  public String deleteById(@PathVariable String recipeId, @PathVariable String id, Model model) {

    log.debug("deleting ingredient recipe id: " + recipeId);
    log.debug("deleting ingredient ingredient id: " + id);

    ingredientService.deleteById(Long.valueOf(recipeId),Long.valueOf(id));

    return "redirect:/recipe/" + recipeId + "/ingredients";
  }
}
