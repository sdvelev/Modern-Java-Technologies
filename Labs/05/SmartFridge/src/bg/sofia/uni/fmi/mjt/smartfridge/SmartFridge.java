package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.comparators.ItemExpirationDecreasingComparator;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SmartFridge implements SmartFridgeAPI {

    Map<Storable, Integer> content;
    int totalCapacity;

    public SmartFridge(int totalCapacity) {
        this.content = new HashMap<>();
        this.totalCapacity = totalCapacity;
    }

    /**
     * Stores some item(s) in the fridge.
     *
     * @param item     the item to store.
     * @param quantity the quantity of the items.
     * @throws IllegalArgumentException        if item is null, or if the quantity is not positive.
     * @throws FridgeCapacityExceededException if there is no free space in the fridge to accommodate the item(s).
     */
    @Override
    public <E extends Storable> void store(E item, int quantity) throws FridgeCapacityExceededException {

        validateIsNull(item, "item");
        validateIsNegativeQuantity(quantity);

        validateIsEnoughCapacity(quantity);

        this.content.put(item, quantity);
    }

    /**
     * Retrieves (and removes) some item(s) from the fridge. Note that if an item has been stored with quantity n,
     * the result contains n elements, one for each item stored.
     *
     * @param itemName the name of the item.
     * @return a list of the retrieved items, ordered by expiration date, starting from the ones
     * that will expire first. If there are no items with the specified name, returns an empty list.
     * If there are items already expired, those are also part of the returned list.
     * @throws IllegalArgumentException if itemName is null, empty or blank.
     */
    @Override
    public List<? extends Storable> retrieve(String itemName) {

        validateIsNullIsEmptyOrIsBlank(itemName);

        boolean found = false;

        for (Storable current : this.content.keySet()) {
            if (current.getName().equals(itemName)) {
                found = true;
                break;
            }
        }

        if (!found) {
            return new ArrayList<Storable>();
        }

        List<Storable> result = new ArrayList<>();

        for (Map.Entry<? extends Storable, Integer> current : this.content.entrySet()) {

            if (current.getKey().getName().equals(itemName)) {
                result.add(current.getKey());
            }
        }

        Collections.sort(result, new ItemExpirationDecreasingComparator());

        return result;
    }

    /**
     * Retrieves (and removes) the specified number of item(s) with the given from the fridge.
     *
     * @param itemName the name of the item(s).
     * @param quantity the quantity of the items to retrieve.
     * @return a list of the retrieved items, containing {@code quantity} elements, ordered by expiration date,
     * starting from the ones that will expire first. If there are items already expired, those are also part of the
     * returned list.
     * @throws IllegalArgumentException      if itemName is null, empty or blank, or of quantity is not positive.
     * @throws InsufficientQuantityException if item with the specified name is not found in the fridge
     *                                       or the stored quantity is insufficient.
     */
    @Override
    public List<? extends Storable> retrieve(String itemName, int quantity) throws InsufficientQuantityException {

        validateIsNullIsEmptyOrIsBlank(itemName);
        validateIsNegativeQuantity(quantity);

        int totalQuantity = getQuantityOfItem(itemName);

        if (totalQuantity == 0 || totalQuantity < quantity) {
            throw new InsufficientQuantityException("item with the specified name is not found in the fridge " +
                "or the stored quantity is insufficient");
        }

        List<Storable> result = new ArrayList<>();

        for (Map.Entry<? extends Storable, Integer> current : this.content.entrySet()) {

            if (current.getKey().getName().equals(itemName) && quantity > 0) {
                result.add(current.getKey());
                --quantity;
            }
        }

        Collections.sort(result, new ItemExpirationDecreasingComparator());

        return result;
    }

    /**
     * Gets the quantity of items with the specified name that are currently stored in the fridge.
     * Note that the quantity includes also potentially expired items.
     *
     * @param itemName
     * @throws IllegalArgumentException if itemName is null, empty or blank.
     */
    @Override
    public int getQuantityOfItem(String itemName) {

        validateIsNullIsEmptyOrIsBlank(itemName);

        int totalQuantity = 0;

        for (Map.Entry<? extends Storable, Integer> current : this.content.entrySet()) {

            if (current.getKey().getName().equals(itemName)) {
                totalQuantity += current.getValue();
            }
        }

        return totalQuantity;
    }

    /**
     * Gets the ingredients that are missing or insufficient in the fridge to prepare the recipe.
     * Note that if some items needed for the recipe are stored in the fridge, but are expired,
     * they cannot be used for preparing the recipe and should be considered missing/insufficient and included
     * in the result. The method though does not remove any expired items from the fridge.
     *
     * @param recipe the recipe
     * @return an iterator of the ingredients missing or insufficient to prepare the recipe.
     * @throws IllegalArgumentException if recipe is null.
     */
    @Override
    public Iterator<Ingredient<? extends Storable>> getMissingIngredientsFromRecipe(Recipe recipe) {

        validateIsNull(recipe, "recipe");

        Set<Ingredient<? extends Storable>> missing = new HashSet<>();

        for (Ingredient<? extends Storable> ingredient : recipe.getIngredients()) {

            int itemQuantity = this.getQuantityOfItem(ingredient.item().getName());

            if (itemQuantity < ingredient.quantity()) {
                missing.add(ingredient);
                continue;
            }

            LocalDate itemExpiredDate = null;

            Iterator<Map.Entry<Storable, Integer>> it = this.content.entrySet().iterator();

            while (it.hasNext()) {
                Map.Entry<Storable, Integer> current = it.next();

                if (current.getKey().getName().equals(ingredient.item().getName())) {

                    itemExpiredDate = current.getKey().getExpiration();
                    break;
                }
            }

            if (itemExpiredDate.isBefore(LocalDate.now())) {
                missing.add(ingredient);
            }

        }

        return missing.iterator();
    }

    /**
     * Removes all expired items from the fridge and returns them as a list, in an undefined order.
     * If there are no expired items stored in the fridge, returns an empty list.
     */
    @Override
    public List<? extends Storable> removeExpired() {

        List<Storable> expiredItems = new ArrayList<>();

        Iterator<Map.Entry<Storable, Integer>> it = this.content.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Storable, Integer> current = it.next();

            if (current.getKey().isExpired()) {

                for (int i = 0; i < current.getValue(); i++) {
                    expiredItems.add(current.getKey());
                }

                it.remove();
            }
        }

        return expiredItems;
    }

    private void validateIsNull(Object o, String title) {

        if (o == null) {
            throw new IllegalArgumentException(title + " is null");
        }
    }

    private void validateIsNegativeQuantity(int quantity) {

        if (quantity <= 0) {
            throw new IllegalArgumentException("quantity is not positive");
        }
    }

    private int getCurrentCapacity() {

        Collection<Integer> values = this.content.values();

        int sum = 0;

        for (Integer i : values) {
            sum += i.intValue();
        }

        return sum;
    }

    private void validateIsEnoughCapacity(int quantity) throws FridgeCapacityExceededException {

        if (this.getCurrentCapacity() + quantity > this.totalCapacity) {
            throw new FridgeCapacityExceededException("there is no free space in the fridge to " +
                "accommodate the item(s)");
        }
    }

    private void validateIsNullIsEmptyOrIsBlank(String itemName) {

        if (itemName == null || itemName.isEmpty() || itemName.isBlank()) {
            throw new IllegalArgumentException("itemName is null, empty or blank");
        }
    }

}
