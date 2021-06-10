package guru.springframework.controllers;

import guru.springframework.domain.Recipe;
import guru.springframework.service.RecipeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class IndexControllerTest {

    @Mock
    RecipeService recipeService;

    @Mock
    Model model;

    IndexController indexController;

    @BeforeEach
    void setUp() {
        recipeService = Mockito.mock(RecipeService.class);
        model = Mockito.mock(Model.class);
        indexController = new IndexController(recipeService);
    }

  @Test
  void testMockMVC() throws Exception {

      MockMvc mockMvc = MockMvcBuilders.standaloneSetup(indexController).build();
      mockMvc.perform(MockMvcRequestBuilders.get("/"))
              .andExpect(status().isOk())
              .andExpect(view().name("index"));
  }

    @Test
    void getIndexPage() {

        //given
        Set<Recipe> recipes = new HashSet<>();

        Recipe recipe = new Recipe();
        recipe.setId(1L);
        recipes.add(new Recipe());
        recipes.add(recipe);

        when(recipeService.getRecipes()).thenReturn(recipes);

        ArgumentCaptor<Set<Recipe>> argumentCaptor = ArgumentCaptor.forClass((Set.class));
        //when
        String viewName = indexController.getIndexPage(model);

        //then
        assertEquals("index",viewName);
        verify(recipeService,times(1)).getRecipes();
        verify(model,times(1)).addAttribute(eq("recipes"),argumentCaptor.capture());

        Set<Recipe> setInController = argumentCaptor.getValue();
        assertEquals(2,setInController.size());
    }
}