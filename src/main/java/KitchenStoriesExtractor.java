import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class KitchenStoriesExtractor
{
   public static String retrieveReceiptContent(String url) throws IOException
   {
      Document html = Jsoup.connect(url).get();
      return null;
   }
}
