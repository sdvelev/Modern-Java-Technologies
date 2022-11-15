package bg.sofia.uni.fmi.mjt.smartfridge.recipe;

import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DefaultRecipe implements Recipe {

    final private Set<Ingredient<? extends Storable>> ingredients;

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

}
