package model;

public class Ingredient
{
   private String name;

   public Ingredient()
   {
   }

   public Ingredient(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @Override
   public String toString()
   {
      return "Name: '" + name;
   }
}
