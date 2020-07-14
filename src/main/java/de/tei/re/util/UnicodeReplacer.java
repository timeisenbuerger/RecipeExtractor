package de.tei.re.util;

import java.util.regex.Pattern;

import com.google.common.base.CharMatcher;
import org.jsoup.internal.StringUtil;

public class UnicodeReplacer
{
   public static String replaceUnicodes(String text)
   {
      Integer digit = null;
      Double unicodeValue = null;
      String unit = null;

      if( text.contains(" ") )
      {
         String[] split = text.split(" ");
         unit = split[split.length - 1];
      }

      if( textContainsDigit(text) )
      {
         digit = Integer.parseInt(CharMatcher.inRange('0', '9').retainFrom(text));
      }

      String regex = ".*[\u00BD\u2154\u00BC\u00BE\u2153].*";
      if( Pattern.matches(regex, text) )
      {
         if( text.contains("\u00BD") )
         {
            unicodeValue = 0.5;
         }

         if( text.contains("\u2154") )
         {
            unicodeValue = 0.66;
         }

         if( text.contains("\u00BC") )
         {
            unicodeValue = 0.25;
         }

         if( text.contains("\u00BE") )
         {
            unicodeValue = 0.75;
         }

         if( text.contains("\u2153") )
         {
            unicodeValue = 0.33;
         }
      }

      if( unicodeValue != null && digit != null )
      {
         text = String.valueOf(digit + unicodeValue);
      }
      else if( digit != null )
      {
         text = digit.toString();
      }
      else if( unicodeValue != null )
      {
         text = unicodeValue.toString();
      }

      if( unit != null )
      {
         text += " " + unit;
      }

      return text;
   }

   private static boolean textContainsDigit(String text)
   {
      boolean result = false;

      for( char c : text.toCharArray() )
      {
         if( StringUtil.isNumeric(c + "") )
         {
            result = true;
            break;
         }
      }

      return result;
   }
}
