package de.tei.re.logic;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.tei.re.model.RotWTemplate;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class KitchenStoriesRotWExtractor
{
   private String pageUrl = "https://www.kitchenstories.com/de/rezepte";
   private Document htmlPage;
   private Element body;

   private List<RotWTemplate> recipesOfTheWeek;

   public KitchenStoriesRotWExtractor() throws IOException
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

      Elements rotW = body.getElementsByClass("archive-tile anim-hover-zoom");

      for( int i = 0; i < 10; i++ )
      {
         result.add(extractSingleRotW(rotW.get(i)));
      }

      return result;
   }

   private RotWTemplate extractSingleRotW(Element singleRotW)
   {
      RotWTemplate rotWTemplate = new RotWTemplate();

      String imageUrl = singleRotW.getElementsByTag("img").get(0).attr("src");
      String recipeTitle = singleRotW.getElementsByTag("h3").get(0).text();
      String recipeUrl = "https://www.kitchstories.com" + singleRotW.getElementsByTag("a").get(0).attr("href");
      String time = singleRotW.getElementsByClass("archive-tile__prep-time").get(0).text().replace(".", "").toLowerCase();

      rotWTemplate.setImageUrl(imageUrl);
      rotWTemplate.setRecipeUrl(recipeUrl);
      rotWTemplate.setTitle(recipeTitle);
      rotWTemplate.setTime(time);

      return rotWTemplate;
   }
}
