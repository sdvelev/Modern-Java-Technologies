package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class DefaultCocktailStorage implements CocktailStorage {

    private Set<Cocktail> cocktails;


    public DefaultCocktailStorage() {

        this.cocktails = new HashSet<>();
    }

    /**
     * Creates a new cocktail recipe
     *
     * @param cocktail cocktail
     * @throws CocktailAlreadyExistsException if the same cocktail already exists
     */
    @Override
    public void createCocktail(Cocktail cocktail) throws CocktailAlreadyExistsException {

        if (this.cocktails.contains(cocktail)) {

            throw new CocktailAlreadyExistsException("There is already such a cocktail");
        }

        this.cocktails.add(cocktail);
    }

    /**
     * Retrieves all cocktail recipes
     *
     * @return all cocktail recipes from the storage, in undefined order.
     * If there are no cocktails in the storage, returns an empty collection.
     */
    @Override
    public Collection<Cocktail> getCocktails() {

        return this.cocktails;
    }

    /**
     * Retrieves all cocktail recipes with given ingredient
     *
     * @param ingredientName name of the ingredient (case-insensitive)
     * @return all cocktail recipes with given ingredient from the storage, in undefined order.
     * If there are no cocktails in the storage with the given ingredient, returns an empty collection.
     */
    @Override
    public Collection<Cocktail> getCocktailsWithIngredient(String ingredientName) {

        return this.cocktails.stream()
            .filter(cocktail -> cocktail.ingredients().contains(new Ingredient(ingredientName, null)))
            .toList();
    }

    /**
     * Retrieves a cocktail recipe with the given name
     *
     * @param name cocktail name (case-insensitive)
     * @return cocktail with the given name
     * @throws CocktailNotFoundException if a cocktail with the given name does not exist in the storage
     */
    @Override
    public Cocktail getCocktail(String name) throws CocktailNotFoundException {

        if (!this.cocktails.contains(new Cocktail(name, null))) {
            throw new CocktailNotFoundException("There is not a cocktail with such a name");
        }

        Cocktail toReturn = null;
        for (Cocktail currentCocktail : this.cocktails) {

            if (currentCocktail.equals(new Cocktail(name, null))) {

                toReturn = currentCocktail;
                break;
            }
        }

        return toReturn;
    }
}
