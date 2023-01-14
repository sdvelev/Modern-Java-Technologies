package bg.sofia.uni.fmi.mjt.cocktail.server.storage;

import bg.sofia.uni.fmi.mjt.cocktail.server.Cocktail;
import bg.sofia.uni.fmi.mjt.cocktail.server.Ingredient;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailAlreadyExistsException;
import bg.sofia.uni.fmi.mjt.cocktail.server.storage.exceptions.CocktailNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

public class DefaultCocktailStorageTest {

    @Test
    void testCreateCocktailSuccessfully() throws CocktailAlreadyExistsException {

        DefaultCocktailStorage defaultCocktailStorage = new DefaultCocktailStorage();

        Cocktail cocktailToAdd = new Cocktail("manhattan", Set.of(new Ingredient("whisky", "100ml"),
            new Ingredient("ice", "1cube")));

        defaultCocktailStorage.createCocktail(cocktailToAdd);

        Assertions.assertTrue(defaultCocktailStorage.getCocktails().contains(cocktailToAdd), "Cocktail is " +
            "created but not stored");
    }

    @Test
    void testGetCocktailsWithIngredientSuccessfully() throws CocktailAlreadyExistsException {

        DefaultCocktailStorage defaultCocktailStorage = new DefaultCocktailStorage();

        Cocktail cocktailToAdd = new Cocktail("manhattan", Set.of(new Ingredient("whisky", "100ml"),
            new Ingredient("ice", "1cube")));

        Cocktail cocktailToAddSecond = new Cocktail("cocktail2", Set.of(new Ingredient("ingr1", "am1"),
            new Ingredient("whisky", "200ml")));

        defaultCocktailStorage.createCocktail(cocktailToAdd);
        defaultCocktailStorage.createCocktail(cocktailToAddSecond);

        List<Cocktail> expectedList = List.of(cocktailToAdd, cocktailToAddSecond);

    Assertions.assertTrue(defaultCocktailStorage.getCocktailsWithIngredient("whisky").
        containsAll(expectedList) && expectedList.containsAll(defaultCocktailStorage.
        getCocktailsWithIngredient("whisky")), "Actual list of cocktails after get-by ingredient" +
        " is not the same as the expected");
    }

    @Test
    void testGetCocktailSuccessfully() throws CocktailAlreadyExistsException, CocktailNotFoundException {

        DefaultCocktailStorage defaultCocktailStorage = new DefaultCocktailStorage();

        Cocktail cocktailToAdd = new Cocktail("manhattan", Set.of(new Ingredient("whisky", "100ml"),
            new Ingredient("ice", "1cube")));

        Cocktail cocktailToAddSecond = new Cocktail("cocktail2", Set.of(new Ingredient("ingr1", "am1"),
            new Ingredient("whisky", "200ml")));

        defaultCocktailStorage.createCocktail(cocktailToAdd);
        defaultCocktailStorage.createCocktail(cocktailToAddSecond);

        Assertions.assertEquals(cocktailToAddSecond, defaultCocktailStorage.getCocktail("cocktail2"),
            "Expected cocktail by get by-name is not the same as the returned");
    }

    @Test
    void testGetCocktailCocktailNotFoundException() {

        DefaultCocktailStorage defaultCocktailStorage = new DefaultCocktailStorage();

        Assertions.assertThrows(CocktailNotFoundException.class, () -> defaultCocktailStorage.
            getCocktail("manhattan"), "CocktailNotFoundException is expected but not thrown");
    }

    @Test
    void testGetCocktailCocktailAlreadyExistsException() throws CocktailAlreadyExistsException {

        DefaultCocktailStorage defaultCocktailStorage = new DefaultCocktailStorage();

        Cocktail cocktailToAdd = new Cocktail("manhattan", Set.of(new Ingredient("whisky", "100ml"),
            new Ingredient("ice", "1cube")));

        defaultCocktailStorage.createCocktail(cocktailToAdd);

        Cocktail cocktailToAddSame = new Cocktail("manhattan", Set.of(new Ingredient("water", "200ml"),
            new Ingredient("ice", "1cube")));

        Assertions.assertThrows(CocktailAlreadyExistsException.class, () -> defaultCocktailStorage.
            createCocktail(cocktailToAddSame), "CocktailAlreadyExistsException is expected but not thrown");
    }
}
