package bg.sofia.uni.fmi.mjt.smartfridge.recipe;

import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.DefaultStorable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DefaultRecipe implements Recipe {

    final private static int CONST1 = 10;
    final private static int CONST2 = 20;
    final private static int CONST3 = 30;
    final private static int CONST4 = 40;

    private Set<Ingredient<? extends Storable>> ingredients;

    public DefaultRecipe() {
        this.ingredients = new HashSet<>();
    }

    /**
     * Gets the ingredients for this recipe.
     */
    @Override
    public Set<Ingredient<? extends Storable>> getIngredients() {
        return this.ingredients;
    }

    /**
     * Adds an ingredient to the recipe. If an ingredient with the same item exists, merges with the existing one
     * by increasing the quantity of the ingredient.
     *
     * @param ingredient the ingredient
     * @throws IllegalArgumentException if ingredient is null.
     */
    @Override
    public void addIngredient(Ingredient<? extends Storable> ingredient) {

        validateIsIngredientNull(ingredient);

        for (Iterator<Ingredient<? extends Storable>> it = this.ingredients.iterator(); it.hasNext();) {

            Ingredient<? extends Storable> currentIngredient = it.next();

            if (currentIngredient.item().equals(ingredient.item())) {

                Storable currentItem = currentIngredient.item();
                int currentQuantity = currentIngredient.quantity();

                it.remove();
                this.ingredients.add(new DefaultIngredient<>(currentItem,
                    currentQuantity + ingredient.quantity()));
                return;
            }
        }

        this.ingredients.add(ingredient);
    }

    /**
     * Removes the ingredient with the respective item name from the recipe.
     * If an ingredient with such item name does not exist, method does nothing.
     *
     * @param itemName the name of the item
     * @throws IllegalArgumentException if itemName is null, empty or blank.
     */
    @Override
    public void removeIngredient(String itemName) {

        validateIsNullIsEmptyOrIsBlank(itemName);

        for (Iterator<Ingredient<? extends Storable>> it = this.ingredients.iterator(); it.hasNext();) {

            Ingredient<? extends Storable> currentIngredient = it.next();

            if (currentIngredient.item().getName().equals(itemName)) {

                it.remove();
                break;
            }
        }

    }

    private void validateIsIngredientNull(Ingredient<? extends Storable> ingredient) {

        if (ingredient == null) {
            throw new IllegalArgumentException("ingredient is null");
        }
    }

    private void validateIsNullIsEmptyOrIsBlank(String itemName) {

        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("itemName is null, empty or blank");
        }
    }

    @Override
    public String toString() {
        return "DefaultRecipe{" +
            "ingredients=" + ingredients +
            '}';
    }

    public static void main(String[] args) {

        DefaultStorable a = new DefaultStorable("Item1", StorableType.BEVERAGE, LocalDate.parse("2022-11-11"));
        DefaultStorable b = new DefaultStorable("Item2", StorableType.BEVERAGE, LocalDate.parse("2022-12-11"));
        DefaultStorable c = new DefaultStorable("Item3", StorableType.BEVERAGE, LocalDate.parse("2022-10-11"));
        DefaultStorable d = new DefaultStorable("Item4", StorableType.BEVERAGE, LocalDate.parse("2022-11-07"));
        DefaultStorable e = new DefaultStorable("Item5", StorableType.BEVERAGE, LocalDate.parse("2021-12-29"));

        Ingredient<DefaultStorable> ing1 = new DefaultIngredient<>(a, CONST1);
        Ingredient<DefaultStorable> ing2 = new DefaultIngredient<>(b, CONST2);
        Ingredient<DefaultStorable> ing3 = new DefaultIngredient<>(c, CONST3);
        Ingredient<DefaultStorable> ing4 = new DefaultIngredient<>(d, CONST4);

        Recipe r = new DefaultRecipe();

        r.addIngredient(ing1);
        r.addIngredient(ing2);
        r.addIngredient(ing3);

        System.out.println(r);

        r.addIngredient(ing1);

        System.out.println(r);

        System.out.println(r.getIngredients());

        r.addIngredient(ing4);

        System.out.println(r);

        r.removeIngredient(e.getName());

        System.out.println(r);
    }

}
