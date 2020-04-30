package de.tei.re;

import java.io.IOException;
import java.util.List;

import de.tei.re.logic.ChefkochExtractor;
import de.tei.re.model.RecipeIngredient;

public class Demo
{
   public static void main(String[] args) throws IOException
   {
      ChefkochExtractor chefkochExtractor = new ChefkochExtractor(
            "https://www.chefkoch.de/rezepte/2507481393507298/Gnocchi-Pfanne.html");
      String title = chefkochExtractor.getTitle();
      String portions = chefkochExtractor.getPortions();
      List<String> tags = chefkochExtractor.getTags();
      String instruction = chefkochExtractor.getInstruction();
      List<RecipeIngredient> recipeIngredients = chefkochExtractor.getRecipeIngredients();
      System.exit(0);
   }
}
