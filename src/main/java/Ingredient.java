public class Ingredient
{
   private String amount;
   private String name;

   public Ingredient()
   {
   }

   public Ingredient(String amount, String name)
   {
      this.amount = amount;
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
      return "Menge: '" + amount + '\'' +
             "Name: '" + name;
   }
}
