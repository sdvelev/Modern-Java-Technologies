package bg.sofia.uni.fmi.mjt.cocktail.server;

import java.util.Objects;

public record Ingredient(String name, String amount) {

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return name.equalsIgnoreCase(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Ingredient{" +
            "name='" + name + '\'' +
            ", amount='" + amount + '\'' +
            '}';
    }
}
