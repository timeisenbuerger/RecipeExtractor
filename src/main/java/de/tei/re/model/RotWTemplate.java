package de.tei.re.model;

public class RotWTemplate
{
   private String recipeUrl;
   private String imageUrl;
   private String title;
   private String difficulty;
   private String time;
   private String shortDescription;

   public String getRecipeUrl()
   {
      return recipeUrl;
   }

   public void setRecipeUrl(String recipeUrl)
   {
      this.recipeUrl = recipeUrl;
   }

   public String getImageUrl()
   {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl)
   {
      this.imageUrl = imageUrl;
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   public String getDifficulty()
   {
      return difficulty;
   }

   public void setDifficulty(String difficulty)
   {
      this.difficulty = difficulty;
   }

   public String getTime()
   {
      return time;
   }

   public void setTime(String time)
   {
      this.time = time;
   }

   public String getShortDescription()
   {
      return shortDescription;
   }

   public void setShortDescription(String shortDescription)
   {
      this.shortDescription = shortDescription;
   }
}
