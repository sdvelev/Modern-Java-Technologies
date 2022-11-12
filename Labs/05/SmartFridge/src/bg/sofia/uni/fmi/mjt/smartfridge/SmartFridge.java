package bg.sofia.uni.fmi.mjt.smartfridge;

import bg.sofia.uni.fmi.mjt.smartfridge.comparators.ItemExpirationDecreasingComparator;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.FridgeCapacityExceededException;
import bg.sofia.uni.fmi.mjt.smartfridge.exception.InsufficientQuantityException;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.DefaultIngredient;
import bg.sofia.uni.fmi.mjt.smartfridge.ingredient.Ingredient;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.DefaultRecipe;
import bg.sofia.uni.fmi.mjt.smartfridge.recipe.Recipe;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.DefaultStorable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

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

    final private static int CONST1 = 10;
    final private static int CONST2 = 20;
    final private static int CONST3 = 30;
    final private static int CONST4 = 40;

    final private static int FOUR = 4;
    final private static int THREE = 3;
    final private static int SIX = 6;
    final private static int FIVE = 5;


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



        //NEW
        validateIsNullIsEmptyOrIsBlank(itemName);

        int totalQuantity = getQuantityOfItem(itemName);

        if (totalQuantity == 0) {
            return new ArrayList<Storable>();
        }


        List<Storable> result = new ArrayList<>();

        Iterator<Map.Entry<Storable, Integer>> it = this.content.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Storable, Integer> current = it.next();


            if (current.getKey().getName().equals(itemName)) {

                for (int i = 0; i < current.getValue(); i++) {
                    result.add(current.getKey());
                }

                it.remove();
            }
        }

        Collections.sort(result, new ItemExpirationDecreasingComparator());

        return result;






























        /*
        validateIsNullIsEmptyOrIsBlank(itemName);

        int totalQuantity = getQuantityOfItem(itemName);

        if (totalQuantity == 0) {
            return new ArrayList<Storable>();
        }

        List<Storable> result = new ArrayList<>();

        Iterator<Map.Entry<Storable, Integer>> it = this.content.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Storable, Integer> current = it.next();

            if (current.getKey().getName().equals(itemName)) {

                for (int i = 0; i < current.getValue(); i++) {

                    result.add(current.getKey());
                }

                it.remove();
            }
        }

        Collections.sort(result, new ItemExpirationDecreasingComparator());

        return result;*/
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



        //NEW
        validateIsNullIsEmptyOrIsBlank(itemName);
        validateIsNegativeQuantity(quantity);

        int totalQuantity = getQuantityOfItem(itemName);

        if (totalQuantity == 0 || totalQuantity < quantity) {
            throw new InsufficientQuantityException("item with the specified name is not found in the fridge " +
                "or the stored quantity is insufficient");
        }

        List<Storable> allMatches = new ArrayList<>();

        Iterator<Map.Entry<Storable, Integer>> it = this.content.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Storable, Integer> current = it.next();


            if (current.getKey().getName().equals(itemName)) {

                for (int i = 0; i < current.getValue(); i++) {
                    allMatches.add(current.getKey());
                }

            }
        }

        Collections.sort(allMatches, new ItemExpirationDecreasingComparator());

        List<Storable> result = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            result.add(allMatches.get(i));
            this.content.put(allMatches.get(i), this.content.get(allMatches.get(i)) - 1);

        }

        it = this.content.entrySet().iterator();

        //Remove selected value
        while (it.hasNext()) {
            Map.Entry<Storable, Integer> current = it.next();

            if (current.getValue() <= 0) {

                it.remove();
            }
        }

        return result;




   /*








































        validateIsNullIsEmptyOrIsBlank(itemName);
        validateIsNegativeQuantity(quantity);

        int totalQuantity = getQuantityOfItem(itemName);

        if (totalQuantity == 0 || totalQuantity < quantity) {
            throw new InsufficientQuantityException("item with the specified name is not found in the fridge " +
                "or the stored quantity is insufficient");
        }

        List<Storable> result = new ArrayList<>();

        Iterator<Map.Entry<Storable, Integer>> it = this.content.entrySet().iterator();

        int counter = 0;

        while (it.hasNext()) {
            Map.Entry<Storable, Integer> current = it.next();


            if (current.getKey().getName().equals(itemName)) {

                for (int i = 0; i < current.getValue(); i++) {
                    result.add(current.getKey());
                    counter++;

                    if (counter == quantity) {
                        this.content.put(current.getKey(), current.getValue() - (i + 1));
                        break;
                    }

                }

                if (counter == quantity) {
                    break;
                }

                it.remove();

            }
        }

        Collections.sort(result, new ItemExpirationDecreasingComparator());

        return result;*/
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

        Map<Storable, Integer> copyContent = this.content;

        Iterator<Map.Entry<Storable, Integer>> it = copyContent.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry<Storable, Integer> current = it.next();

            if (current.getKey().isExpired()) {

                it.remove();
            }
        }

        for (Ingredient<? extends Storable> ingredient : recipe.getIngredients()) {

            int itemQuantity = 0;

            for (Map.Entry<? extends Storable, Integer> current : copyContent.entrySet()) {

                if (current.getKey().getName().equals(ingredient.item().getName())) {
                    itemQuantity += current.getValue();
                }
            }

            if (itemQuantity < ingredient.quantity()) {
                missing.add(new DefaultIngredient<>(ingredient.item(), ingredient.quantity() - itemQuantity));
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



    public static void main(String[] args) throws FridgeCapacityExceededException, InsufficientQuantityException {


        DefaultStorable a = new DefaultStorable("Item1", StorableType.BEVERAGE, LocalDate.parse("2029-11-11"));
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


        SmartFridge sf = new SmartFridge(CONST2);

        System.out.println(sf.getQuantityOfItem("abv"));

        sf.store(new DefaultStorable("asbr", StorableType.FOOD, LocalDate.parse("2020-11-11")), FOUR);

        sf.store(a, SIX);

        System.out.println(sf.getCurrentCapacity());

        sf.removeExpired();

        System.out.println(sf.getCurrentCapacity());

        sf.store(b, THREE);

        System.out.println(sf.getCurrentCapacity());



        System.out.println(sf.retrieve("Item1", 2));

        System.out.println(sf.getCurrentCapacity());

        DefaultStorable h = new DefaultStorable("Item1", StorableType.BEVERAGE, LocalDate.parse("2020-11-07"));

        sf.store(h, FOUR);

        System.out.println(sf.getCurrentCapacity());

        System.out.println(sf.retrieve("Item1", THREE));

        System.out.println(sf.getCurrentCapacity());

        System.out.println(sf.retrieve("Item1", THREE));

        System.out.println(sf.getCurrentCapacity());

        System.out.println(sf.retrieve("Item2"));

        System.out.println(sf.getCurrentCapacity());

        DefaultStorable j = new DefaultStorable("Item1", StorableType.BEVERAGE, LocalDate.parse("2018-11-07"));

        sf.store(j, SIX);

        System.out.println(sf.getCurrentCapacity());

    //    System.out.println(sf.retrieve("Item1"));

        Iterator<Ingredient<? extends Storable>> fg = sf.getMissingIngredientsFromRecipe(r);

        while (fg.hasNext()) {
            fg.next();
            System.out.println(fg);
        }

    }
}
