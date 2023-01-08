package bg.sofia.uni.fmi.mjt.cocktail.server;

import java.util.Objects;
import java.util.Set;

public record Cocktail(String name, Set<Ingredient> ingredients) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cocktail cocktail = (Cocktail) o;
        return name.equalsIgnoreCase(cocktail.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Cocktail{" +
            "name='" + name + '\'' +
            ", ingredients=" + ingredients +
            '}';
    }
}
