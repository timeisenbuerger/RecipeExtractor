import java.io.IOException;

public class Main
{
   public static void main(String[] args) throws IOException
   {
      String content = ChefkochExtractor.retrieveReceiptContent(
            "https://www.chefkoch.de/rezepte/460771139185700/Schnelles-Thai-Curry-mit-Huhn-Paprika-und-feiner-Erdnussnote.html");
      System.out.println(content);
      System.exit(0);
   }
}
