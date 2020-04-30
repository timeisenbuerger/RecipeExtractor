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
            "https://www.google.com/amp/s/www.chefkoch.de/amp/rezepte/745721177147257/Lasagne.html");
      String title = chefkochExtractor.getTitle();
      String portions = chefkochExtractor.getPortions();
      List<String> tags = chefkochExtractor.getTags();
      String instruction = chefkochExtractor.getInstruction();
      List<RecipeIngredient> recipeIngredients = chefkochExtractor.getRecipeIngredients();
      System.exit(0);
   }
}
