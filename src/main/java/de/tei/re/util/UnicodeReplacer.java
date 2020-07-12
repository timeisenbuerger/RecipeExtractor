package de.tei.re.util;

import java.util.regex.Pattern;

public class UnicodeReplacer
{
   public static String replaceUnicodes(String text)
   {
      String regex = ".*[\u00BD\u2154\u00BC\u00BE\u2153].*";
      if( Pattern.matches(regex, text) )
      {
         if( text.contains("\u00BD") )
         {
            text = text.replace("\u00BD", "0.5");
         }

         if( text.contains("\u2154") )
         {
            text = text.replace("\u2154", "0.66");
         }

         if( text.contains("\u00BC") )
         {
            text = text.replace("\u00BC", "0.25");
         }

         if( text.contains("\u00BE") )
         {
            text = text.replace("\u00BE", "0.75");
         }

         if( text.contains("\u2153") )
         {
            text = text.replace("\u2153", "0.33");
         }
      }

      return text;
   }
}
