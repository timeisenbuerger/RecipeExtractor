package de.tei.re.model;

public class RecipeIngredient
{
   private String amount;
   private String unit;
   private String name;

   public RecipeIngredient()
   {
   }

   public RecipeIngredient(String amount, String unit, String name)
   {
      this.amount = amount;
      this.unit = unit;
      this.name = name;
   }

   public String getAmount()
   {
      return amount;
   }

   public void setAmount(String amount)
   {
      this.amount = amount;
   }

   public String getUnit()
   {
      return unit;
   }

   public void setUnit(String unit)
   {
      this.unit = unit;
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
      return amount + " " + unit + " " + name;
   }
}
