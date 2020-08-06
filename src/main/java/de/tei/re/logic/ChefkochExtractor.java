package de.tei.re.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import de.tei.re.model.IngredientTableItem;
import de.tei.re.model.RecipeIngredient;
import de.tei.re.util.UnicodeReplacer;
import org.jsoup.Jsoup;
import org.jsoup.internal.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ChefkochExtractor
{
   private String recipeUrl;
   private Document recipeHtmlPage;
   private Element body;

   private String imageLink;
   private String title;
   private String difficulty;
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

   public String getDifficulty()
   {
      if( difficulty == null )
      {
         difficulty = extractDifficulty();
      }
      return difficulty;
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

   public void changeRecipe(String recipeUrl) throws IOException
   {
      this.recipeUrl = recipeUrl;
      this.recipeHtmlPage = Jsoup.connect(recipeUrl).get();
      this.body = recipeHtmlPage.body();

      imageLink = null;
      title = null;
      portions = null;
      instruction = null;
      tags = null;
      recipeIngredients = null;
   }

   private String extractImageLink()
   {
      String result = null;

      Elements elementsByTag = recipeHtmlPage.body().getElementsByTag("amp-img");
      for( int i = 0; i < elementsByTag.size(); i++ )
      {
         Element element = elementsByTag.get(i);
         if( element.hasAttr("srcset") )
         {
            result = element.attr("src");
            break;
         }
      }

      return result;
   }

   private String extractTitle()
   {
      return recipeHtmlPage.body().getElementsByTag("h1").get(0).text();
   }

   private String extractPortions()
   {
      return body.getElementsByAttributeValue("aria-label", "Anzahl der Portionen").get(0).attr("value");
   }

   private String extractDifficulty()
   {
      String result = null;

      result = body.getElementsByClass("recipe-difficulty").get(0).text().split(" ")[1];
      if( result.equals("simpel") )
      {
         result = "Einfach";
      }
      else if( result.equals("normal") )
      {
         result = "Mittel";
      }
      else if( result.equals("pfiffig") )
      {
         result = "Schwer";
      }

      return result;
   }

   private List<String> extractTags()
   {
      Map<String, String> tags = new HashMap<>();
      Element tagElements = body.getElementsByClass("ds-recipe-meta rds-recipe-meta").get(0);

      for( Node child : tagElements.childNodes() )
      {
         Element element = (Element) child;
         if( element.tagName().equals("span") )
         {
            String text = element.text();
            for( int i = 0; i < text.toCharArray().length; i++ )
            {
               char c = text.toCharArray()[i];
               if( StringUtil.isNumeric(String.valueOf(c)) )
               {
                  tags.put(text.substring(0, i - 1).trim(), text.substring(i));
                  break;
               }
            }
         }
      }

      Pattern pattern = Pattern.compile("^(?=.)(?:(?<hours>\\d+) *Stunde?)?(?: *(?<minutes>\\d+) *Minuten?)?$");
      Pattern pattern2 = Pattern.compile("^(?=.)(?:(?<hours>\\d+) *Stunden?)?(?: *(?<minutes>\\d+) *Minuten?)?$");

      List<String> resultList = new ArrayList<>();
      for( Map.Entry<String, String> entry : tags.entrySet() )
      {
         String tag = entry.getValue();
         Matcher matcher1 = pattern.matcher(tag);
         Matcher matcher2 = pattern2.matcher(tag);

         String hours, minutes;
         if( matcher1.matches() )
         {
            hours = matcher1.group("hours");
            minutes = matcher1.group("minutes");
         }
         else if( matcher2.matches() )
         {
            hours = matcher1.group("hours");
            minutes = matcher1.group("minutes");
         }
         else
         {
            resultList.add(tag);
            break;
         }

         Integer hoursVal, minutesVal, result = null;
         if( hours != null )
         {
            hoursVal = Integer.parseInt(hours) * 60;
            result = hoursVal;
         }
         if( minutes != null )
         {
            minutesVal = Integer.parseInt(minutes);
            if( result == null )
            {
               result = minutesVal;
            }
            else
            {
               result += minutesVal;
            }
         }
         resultList.add(entry.getKey() + " " + result + " min");
      }

      return resultList;
   }

   private String extractInstruction()
   {
      Elements elements = body.getElementsByClass("ds-box");
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
      List<IngredientTableItem> ingredients = new ArrayList<>();

      for( Element ingredientsTable : body.getElementsByClass("ingredients table-header") )
      {
         ingredients = extractIngredients(ingredients, ingredientsTable);
      }

      Iterator<IngredientTableItem> iterator = ingredients.iterator();
      while( iterator.hasNext() )
      {
         IngredientTableItem next = iterator.next();

         String amount, unit, ingredient;

         if( !next.getLeft().isEmpty() )
         {
            next.setLeft(UnicodeReplacer.replaceUnicodes(next.getLeft().trim()));
         }

         if( next.getLeft().contains(" ") )
         {
            String[] parts = next.getLeft().split(" ");
            amount = parts[0];
            unit = parts[1];
         }
         else
         {
            amount = next.getLeft();
            unit = "";
         }

         ingredient = next.getRight();
         recipeIngredients.add(new RecipeIngredient(amount, unit, ingredient));
      }

      return recipeIngredients;
   }

   private List<IngredientTableItem> extractIngredients(List<IngredientTableItem> ingredients, Element node)
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
                  if( element.parent().className().equals("td-left") )
                  {
                     ingredients.add(new IngredientTableItem(element.text(), null));
                  }
                  else if( element.parent().className().equals("td-right") )
                  {
                     if( ingredients.isEmpty() )
                     {
                        ingredients.add(new IngredientTableItem("", element.text()));
                     }
                     else
                     {
                        IngredientTableItem item = ingredients.get(ingredients.size() - 1);
                        if( item.getRight() != null )
                        {
                           ingredients.add(new IngredientTableItem("", element.text()));
                        }
                        else
                        {
                           item.setRight(element.text());
                        }
                     }
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
