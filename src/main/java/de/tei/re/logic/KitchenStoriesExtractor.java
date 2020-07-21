package de.tei.re.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import de.tei.re.model.Ingredient;
import de.tei.re.model.IngredientTableItem;
import de.tei.re.model.RecipeIngredient;
import de.tei.re.util.UnicodeReplacer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class KitchenStoriesExtractor
{
   private String recipeUrl;
   private Document recipeHtmlPage;
   private Element body;

   private String imageLink;
   private String title;
   private List<String> times;
   private String difficulty;
   private String portions;
   private List<String> steps;
   private List<RecipeIngredient> recipeIngredients;

   public KitchenStoriesExtractor()
   {
   }

   public KitchenStoriesExtractor(String recipeUrl) throws IOException
   {
      this.recipeUrl = recipeUrl;
      this.recipeHtmlPage = Jsoup.connect(recipeUrl).get();
      this.body = recipeHtmlPage.body();
   }

   public String getImageLink()
   {
      if( imageLink == null )
      {
         imageLink = extractImageLink();
      }
      return imageLink;
   }

   public String getTitle()
   {
      if( title == null )
      {
         title = extractTitle();
      }
      return title;
   }

   public String getRecipeUrl()
   {
      return recipeUrl;
   }

   public Document getRecipeHtmlPage()
   {
      return recipeHtmlPage;
   }

   public String getPortions()
   {
      if( portions == null )
      {
         portions = extractPortions();
      }
      return portions;
   }

   public List<String> getTimes()
   {
      if( times == null )
      {
         times = extractTimes();
      }
      return times;
   }

   public String getDifficulty()
   {
      if( difficulty == null )
      {
         difficulty = extractDifficulty();
      }
      return difficulty;
   }

   public List<String> getSteps()
   {
      if( steps == null )
      {
         steps = extractSteps();
      }
      return steps;
   }

   public List<RecipeIngredient> getRecipeIngredients()
   {
      if( recipeIngredients == null )
      {
         recipeIngredients = extractIngredients();
      }
      return recipeIngredients;
   }

   public void changeRecipe(String recipeUrl) throws IOException
   {
      this.recipeUrl = recipeUrl;
      this.recipeHtmlPage = Jsoup.connect(recipeUrl).get();
      this.body = recipeHtmlPage.body();

      imageLink = null;
      title = null;
      portions = null;
      steps = null;
      times = null;
      difficulty = null;
      recipeIngredients = null;
   }

   private String extractImageLink()
   {
      String result = null;

      Elements elementsByClass = body.getElementsByClass("page-header__image");
      if( elementsByClass.size() > 0 )
      {
         result = body.getElementsByClass("page-header__image").get(0).attr("src");
      }

      return result;
   }

   private String extractTitle()
   {
      return recipeHtmlPage.body().getElementsByTag("h1").get(0).text();
   }

   private String extractPortions()
   {
      String result = null;
      Elements elementsByClass = body.getElementsByClass("stepper-value");
      if( elementsByClass.size() == 1 )
      {
         result = elementsByClass.get(0).text();
      }

      return result;
   }

   private List<String> extractTimes()
   {
      List<String> resultList = new ArrayList<>();

      Elements time = body.getElementsByClass("time");
      Elements unit = body.getElementsByClass("unit");

      for( int i = 0; i < 3; i++ )
      {
         String result;
         result = time.get(i).text();
         result += " " + unit.get(i).text();

         resultList.add(result);
      }

      return resultList;
   }

   private String extractDifficulty()
   {
      String result = null;

      Elements difficulty = body.getElementsByClass("recipe-difficulty");
      result = difficulty.get(0).child(1).text().split(" ")[0];

      return result;
   }

   private List<RecipeIngredient> extractIngredients()
   {
      List<RecipeIngredient> resultList = new ArrayList<>();

      Elements ingredientsTable = body.getElementsByClass("ingredients");
      List<Node> rows = ingredientsTable.get(0).childNodes().get(0).childNodes();
      List<IngredientTableItem> ingredientTableItemList = new ArrayList<>();
      for( Node row : rows )
      {
         List<Node> items = row.childNodes();
         IngredientTableItem ingredientTableItem = new IngredientTableItem();

         for( int i = 0; i < items.size(); i++ )
         {
            if( i == 0 )
            {
               List<Node> childNodes = row.childNodes().get(i).childNodes();
               if( childNodes.size() > 0 )
               {
                  String text = ((TextNode) childNodes.get(i)).text();
                  ingredientTableItem.setLeft(text);
               }
               else
               {
                  ingredientTableItem.setLeft("");
               }
            }
            else if( i == 1 )
            {
               String text = ((TextNode) row.childNodes().get(i).childNodes().get(0)).text();
               ingredientTableItem.setRight(text);
            }
         }

         ingredientTableItemList.add(ingredientTableItem);
      }

      for( IngredientTableItem ingredientTableItem : ingredientTableItemList )
      {
         String amount = "";
         String unit = "";
         String ingredient = "";

         if( !ingredientTableItem.getLeft().isEmpty() )
         {
            ingredientTableItem.setLeft(UnicodeReplacer.replaceUnicodes(ingredientTableItem.getLeft().trim()));

            String[] parts = ingredientTableItem.getLeft().split(" ");
            amount = parts[0];

            if( parts.length > 1 )
            {
               unit = parts[1];
            }
         }

         ingredient = ingredientTableItem.getRight();

         resultList.add(new RecipeIngredient(amount, unit, ingredient));
      }

      return resultList;
   }

   public List<String> extractSteps()
   {
      List<String> resultList = new ArrayList<>();

      Elements steps = body.getElementsByClass("step");
      for( int i = 0; i < steps.size(); i++ )
      {
         String text = "Schritt " + (i + 1) + "/" + steps.size() + ":\n\n" + steps.get(i).getElementsByClass("text").get(0).text() + "\n\n";
         resultList.add(text);
      }

      return resultList;
   }
}
