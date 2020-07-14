package de.tei.re;

import java.io.IOException;
import java.util.List;

import de.tei.re.logic.ChefkochExtractor;
import de.tei.re.logic.ChefkochRotWExtractor;
import de.tei.re.logic.KitchenStoriesExtractor;
import de.tei.re.logic.KitchenStoriesRotWExtractor;
import de.tei.re.model.RecipeIngredient;

public class Demo
{
   public static void main(String[] args) throws IOException
   {
      extractChefkochRecipe();
      extractKitchenStoriesRecipe();
      extractChefkochRecipesOfTheWeek();
      extractKitchenStoriesRecipesOfTheWeek();
      System.exit(0);
   }

   private static void extractChefkochRecipe() throws IOException
   {
      ChefkochExtractor chefkochExtractor = new ChefkochExtractor(
            "https://www.chefkoch.de/rezepte/2516591394803059/Pikanter-Kartoffelpuffer-Kuchen-a-la-Gaga.html");
      String imageLink = chefkochExtractor.getImageLink();
      String title = chefkochExtractor.getTitle();
      String difficulty = chefkochExtractor.getDifficulty();
      String portions = chefkochExtractor.getPortions();
      List<String> tags = chefkochExtractor.getTags();
      String instruction = chefkochExtractor.getInstruction();
      List<RecipeIngredient> recipeIngredients = chefkochExtractor.getRecipeIngredients();
   }

   private static void extractKitchenStoriesRecipe() throws IOException
   {
      KitchenStoriesExtractor kitchenStoriesExtractor = new KitchenStoriesExtractor(
            "https://www.kitchenstories.com/de/rezepte/5-zutaten-spicy-shrimp-salat-sandwich");

      kitchenStoriesExtractor.getImageLink();
      kitchenStoriesExtractor.getTitle();
      kitchenStoriesExtractor.getPortions();
      kitchenStoriesExtractor.getTimes();
      kitchenStoriesExtractor.getDifficulty();
      kitchenStoriesExtractor.getRecipeIngredients();
      kitchenStoriesExtractor.getSteps();
   }

   private static void extractChefkochRecipesOfTheWeek() throws IOException
   {
      ChefkochRotWExtractor chefkochRotWExtractor = new ChefkochRotWExtractor();
      chefkochRotWExtractor.getRecipesOfTheWeek();
   }

   private static void extractKitchenStoriesRecipesOfTheWeek() throws IOException
   {
      KitchenStoriesRotWExtractor kitchenStoriesRotWExtractor = new KitchenStoriesRotWExtractor();
      kitchenStoriesRotWExtractor.getRecipesOfTheWeek();
   }
}
