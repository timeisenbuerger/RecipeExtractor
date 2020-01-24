import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class ChefkochExtractor
{
   public static String retrieveReceiptContent(String url) throws IOException
   {
      Document html = Jsoup
            .connect(url).get();

      String portions = html.body().getElementsByAttributeValue("aria-label", "Anzahl der Portionen").get(0).attr("value");

      Element ingredientsTable = html.body().getElementsByClass("ingredients table-header").get(0);
      List<String> ingredientStrings = collectIngredients(new ArrayList<>(), ingredientsTable);

      List<Ingredient> ingredients = new ArrayList<>();
      for( int i = 0; i < ingredientStrings.size(); i += 2 )
      {
         ingredients.add(new Ingredient(ingredientStrings.get(i), ingredientStrings.get(i + 1)));
      }

      List<String> tags = collectTags(html);

      String preparation = collectPreparation(html);

      String content = "";
      content = content + "<h2>Portionen: </h2>" + portions + "\n";
      content = content + "<h2>Zutaten</h2>\n";
      for( Ingredient ingredient : ingredients )
      {
         content = content + ingredient.getAmount() + "   " + ingredient.getName() + "<br>";
      }

      content = content + "<h2>Zubereitung</h2>\n";

      for( int i = 0; i < tags.size(); i++ )
      {
         String tag = tags.get(i);
         if( i < tags.size() - 1 )
         {
            content = content + tag;
         }
         else
         {
            content = content + tag + "<br><br>";
         }
      }

      content = content + preparation;

      return content;
   }

   private static List<String> collectTags(Document html)
   {
      List<String> tags = new ArrayList<>();
      Element tagElements = html.body().getElementsByClass("ds-recipe-meta rds-recipe-meta").get(0);

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

   private static String collectPreparation(Document html)
   {
      Elements elements = html.body().getElementsByClass("ds-box");
      Element preperation = null;
      for( Element element : elements )
      {
         if( element.parent().className().equals("ds-box ds-grid-float ds-col-12 ds-col-m-8  ds-or-3") )
         {
            preperation = element;
            break;
         }
      }
      return preperation.text();
   }

   private static List<String> collectIngredients(List<String> ingredients, Element node)
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
                     collectIngredients(ingredients, (Element) child);
                  }
               }
            }
         }
      }

      return ingredients;
   }
}
