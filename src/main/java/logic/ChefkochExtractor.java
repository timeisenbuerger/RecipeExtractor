package logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.RecipeIngredient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ChefkochExtractor
{
   private String recipeUrl;
   private Document recipeHtmlPage;

   private String title;
   private String portions;
   private String instruction;
   private List<String> tags;
   private List<RecipeIngredient> recipeIngredients;

   public ChefkochExtractor()
   {
   }

   public ChefkochExtractor(String recipeUrl) throws IOException
   {
      this.recipeUrl = recipeUrl;
      this.recipeHtmlPage = Jsoup.connect(recipeUrl).get();
   }

   //public static String retrieveReceiptContent(String url) throws IOException
   //{
   //   Document html = Jsoup
   //         .connect(url).get();
   //
   //   String portions = html.body().getElementsByAttributeValue("aria-label", "Anzahl der Portionen").get(0).attr("value");
   //
   //   Element ingredientsTable = html.body().getElementsByClass("ingredients table-header").get(0);
   //   List<String> ingredientStrings = extractIngredients(new ArrayList<>(), ingredientsTable);
   //
   //   List<RecipeIngredient> ingredients = new ArrayList<>();
   //   for( int i = 0; i < ingredientStrings.size(); i += 2 )
   //   {
   //      ingredients.add(new RecipeIngredient(ingredientStrings.get(i), ingredientStrings.get(i + 1)));
   //   }
   //
   //   List<String> tags = extractTags(html);
   //
   //   String preparation = extractInstruction(html);
   //
   //   String content = "";
   //   content = content + "<h2>Portionen: </h2>" + portions + "\n";
   //   content = content + "<h2>Zutaten</h2>\n";
   //   for( RecipeIngredient ingredient : ingredients )
   //   {
   //      content = content + ingredient.getAmount() + "   " + ingredient.getName() + "<br>";
   //   }
   //
   //   content = content + "<h2>Zubereitung</h2>\n";
   //
   //   for( int i = 0; i < tags.size(); i++ )
   //   {
   //      String tag = tags.get(i);
   //      if( i < tags.size() - 1 )
   //      {
   //         content = content + tag;
   //      }
   //      else
   //      {
   //         content = content + tag + "<br><br>";
   //      }
   //   }
   //
   //   content = content + preparation;
   //
   //   return content;
   //}

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

   public List<String> getTags()
   {
      if( tags == null )
      {
         tags = extractTags();
      }
      return tags;
   }

   public String getInstruction()
   {
      if( instruction == null )
      {
         instruction = extractInstruction();
      }
      return instruction;
   }

   public List<RecipeIngredient> getRecipeIngredients()
   {
      if( recipeIngredients == null )
      {
         recipeIngredients = extractIngredients();
      }
      return recipeIngredients;
   }

   private void changeRecipe(String recipeUrl) throws IOException
   {
      this.recipeUrl = recipeUrl;
      this.recipeHtmlPage = Jsoup.connect(recipeUrl).get();

      title = null;
      portions = null;
      instruction = null;
      tags = null;
      recipeIngredients = null;
   }

   private String extractTitle()
   {
      return recipeHtmlPage.body().getElementsByTag("h1").get(0).text();
   }

   private String extractPortions()
   {
      return recipeHtmlPage.body().getElementsByAttributeValue("aria-label", "Anzahl der Portionen").get(0).attr("value");
   }

   private List<String> extractTags()
   {
      List<String> tags = new ArrayList<>();
      Element tagElements = recipeHtmlPage.body().getElementsByClass("ds-recipe-meta rds-recipe-meta").get(0);

      for( Node child : tagElements.childNodes() )
      {
         Element element = (Element) child;
         if( element.tagName().equals("span") )
         {
            tags.add(element.text());
         }
      }
      return tags;
   }

   private String extractInstruction()
   {
      Elements elements = recipeHtmlPage.body().getElementsByClass("ds-box");
      Element instruction = null;
      for( Element element : elements )
      {
         if( element.parent().className().equals("ds-box ds-grid-float ds-col-12 ds-col-m-8  ds-or-3") )
         {
            instruction = element;
            break;
         }
      }
      return instruction.text();
   }

   private List<RecipeIngredient> extractIngredients()
   {
      List<RecipeIngredient> recipeIngredients = new ArrayList<>();
      List<String> ingredients = new ArrayList<>();
      Element ingredientsTable = recipeHtmlPage.body().getElementsByClass("ingredients table-header").get(0);

      ingredients = extractIngredients(ingredients, ingredientsTable);

      for( int i = 0; i < ingredients.size(); i += 2 )
      {
         recipeIngredients.add(new RecipeIngredient(ingredients.get(i), ingredients.get(i + 1)));
      }

      return recipeIngredients;
   }

   private List<String> extractIngredients(List<String> ingredients, Element node)
   {
      if( !node.childNodes().isEmpty() )
      {
         for( Node child : node.childNodes() )
         {
            if( child instanceof Element )
            {
               Element element = (Element) child;
               if( element.parent().tagName().equals("td") && element.tagName().equals("span") )
               {
                  if( element.hasText() )
                  {
                     ingredients.add(element.text());
                  }
               }
               else
               {
                  if( !element.childNodes().isEmpty() )
                  {
                     extractIngredients(ingredients, (Element) child);
                  }
               }
            }
         }
      }

      return ingredients;
   }
}
