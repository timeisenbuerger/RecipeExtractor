package de.tei.re.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tei.re.model.RotWTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ChefkochRotWExtractor
{
   private String pageUrl = "https://www.chefkoch.de/rezept-des-tages/";
   private Document htmlPage;
   private Element body;

   private List<RotWTemplate> recipesOfTheWeek;

   public ChefkochRotWExtractor() throws IOException
   {
      this.htmlPage = Jsoup.connect(pageUrl).get();
      this.body = htmlPage.body();
   }

   public List<RotWTemplate> getRecipesOfTheWeek()
   {
      if( recipesOfTheWeek == null )
      {
         recipesOfTheWeek = extractRecipesOfTheWeek();
      }

      return recipesOfTheWeek;
   }

   private List<RotWTemplate> extractRecipesOfTheWeek()
   {
      List<RotWTemplate> result = new ArrayList<>();

      Elements currentRotW = body.getElementsByClass("card card-recipe recipe--today");
      Elements lastRotW = body.getElementsByClass("card card-recipe");

      result.add(extractSingleRotW(currentRotW.get(0)));
      for( int i = 0; i < lastRotW.size(); i++ )
      {
         result.add(extractSingleRotW(lastRotW.get(i)));
      }

      return result;
   }

   private RotWTemplate extractSingleRotW(Element singleRotW)
   {
      RotWTemplate rotWTemplate = new RotWTemplate();

      for( Element element : singleRotW.getAllElements() )
      {
         if( element.className().equals("card__picture") )
         {
            String imageLink = element.getElementsByTag("img").get(0).attr("src");
            rotWTemplate.setImageUrl(imageLink);
         }
         else if( element.className().equals("card__main") )
         {
            String recipeUrl = "https://www.chefkoch.de" + element.getElementsByTag("a").get(0).attr("href");
            String recipeTitle = element.child(1).getElementsByTag("h3").get(0).text();
            String shortDescription = element.getElementsByClass("recipe-subtitle").get(0).text();
            String difficulty = element.getElementsByClass("meta--badge").get(0).text();
            String time = element.getElementsByClass("meta--clock").get(0).text().replace(".", "").toLowerCase();

            if( difficulty.equals("simpel") )
            {
               difficulty = "Einfach";
            }
            else if( difficulty.equals("normal") )
            {
               difficulty = "Mittel";
            }
            else if( difficulty.equals("pfiffig") )
            {
               difficulty = "Schwer";
            }

            rotWTemplate.setRecipeUrl(recipeUrl);
            rotWTemplate.setTitle(recipeTitle);
            rotWTemplate.setShortDescription(shortDescription);
            rotWTemplate.setDifficulty(difficulty);
            rotWTemplate.setTime(time);
         }
      }

      return rotWTemplate;
   }
}
