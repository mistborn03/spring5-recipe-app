package guru.springframework.controllers;

import guru.springframework.commands.IngredientCommand;
import guru.springframework.commands.RecipeCommand;
import guru.springframework.service.IngredientService;
import guru.springframework.service.RecipeService;
import guru.springframework.service.UnitOfMeasureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class IngredientControllerTest {

  @Mock RecipeService recipeService;

  @Mock IngredientService ingredientService;

  @Mock UnitOfMeasureService unitOfMeasureService;

  IngredientController controller;

  MockMvc mockMvc;

  @BeforeEach
  void setUp() {

    controller = new IngredientController(recipeService, ingredientService, unitOfMeasureService);
    mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
  }

  @Test
  void testListIngredients() throws Exception {

    RecipeCommand recipeCommand = new RecipeCommand();
    when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);

    mockMvc
        .perform(get("/recipe/1/ingredients"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/ingredient/list"))
        .andExpect(model().attributeExists("recipe"));

    verify(recipeService, times(1)).findCommandById(anyLong());
  }

  @Test
  void testShowIngredient() throws Exception {
    IngredientCommand ingredientCommand = new IngredientCommand();

    when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong()))
        .thenReturn(ingredientCommand);

    mockMvc
        .perform(get("/recipe/1/ingredient/2/show"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/ingredient/show"))
        .andExpect(model().attributeExists("ingredient"));
  }

  @Test
  public void testUpdateIngredientForm() throws Exception {

    IngredientCommand ingredientCommand = new IngredientCommand();

    when(ingredientService.findByRecipeIdAndIngredientId(anyLong(), anyLong()))
        .thenReturn(ingredientCommand);
    when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

    mockMvc
        .perform(get("/recipe/1/ingredient/2/update"))
        .andExpect(status().isOk())
        .andExpect(view().name("recipe/ingredient/ingredientform"))
        .andExpect(model().attributeExists("ingredient"))
        .andExpect(model().attributeExists("uomList"));
  }

  @Test
  public void testSaveOrUpdate() throws Exception {

    IngredientCommand command = new IngredientCommand();
    command.setId(3L);
    command.setRecipeId(2L);

    when(ingredientService.saveIngredientCommand(any())).thenReturn(command);

    mockMvc
        .perform(
            post("/recipe/2/ingredient")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("id", "")
                .param("description", ""))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/recipe/2/ingredient/3/show"));
  }

  @Test
  public void testNewIngredientForm() throws Exception {
    RecipeCommand recipeCommand = new RecipeCommand();
    recipeCommand.setId(1L);

    when(recipeService.findCommandById(anyLong())).thenReturn(recipeCommand);
    when(unitOfMeasureService.listAllUoms()).thenReturn(new HashSet<>());

    mockMvc
        .perform(get("/recipe/1/ingredient/new"))
        .andExpect(status().isOk())
        .andExpect(model().attributeExists("ingredient"))
        .andExpect(model().attributeExists("uomList"))
        .andExpect(view().name("recipe/ingredient/ingredientform"));

    verify(recipeService, times(1)).findCommandById(anyLong());
  }

  @Test
  public void testDeleteIngredient() throws Exception {

    mockMvc
        .perform(get("/recipe/1/ingredient/3/delete"))
        .andExpect(status().is3xxRedirection())
        .andExpect(view().name("redirect:/recipe/1/ingredients"));

    verify(ingredientService, times(1)).deleteById(anyLong(), anyLong());
  }
}
