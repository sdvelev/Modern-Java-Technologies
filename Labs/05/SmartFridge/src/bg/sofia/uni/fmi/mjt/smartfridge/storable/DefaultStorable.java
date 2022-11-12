package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.comparators.ItemExpirationDecreasingComparator;
import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class DefaultStorable implements Storable {

    private String name;
    private StorableType type;
    private LocalDate expiration;

    public DefaultStorable(String name, StorableType type, LocalDate expiration) {

        this.name = name;
        this.type = type;
        this.expiration = expiration;
    }

    /**
     * Gets the type of this storable.
     */
    @Override
    public StorableType getType() {

        return this.type;
    }

    /**
     * Gets the name of this storable.
     */
    @Override
    public String getName() {

        return this.name;
    }

    /**
     * Gets the expiration date of this storable.
     */
    @Override
    public LocalDate getExpiration() {

        return this.expiration;
    }

    /**
     * Returns true, if the storable is expired.
     */
    @Override
    public boolean isExpired() {

        return this.expiration.isBefore(LocalDate.now());
    }

   /* @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultStorable that = (DefaultStorable) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "ItemStorable{" +
            "name='" + name + '\'' +
            ", type=" + type +
            ", expiration=" + expiration +
            '}';
    }*/

    @Override
    public String toString() {
        return "DefaultStorable{" +
            "name='" + name + '\'' +
            ", type=" + type +
            ", expiration=" + expiration +
            '}';
    }

    public static void main(String[] args) {
        DefaultStorable a = new DefaultStorable("Item1", StorableType.BEVERAGE, LocalDate.parse("2022-11-11"));
        DefaultStorable b = new DefaultStorable("Item2", StorableType.BEVERAGE, LocalDate.parse("2022-12-11"));
        DefaultStorable c = new DefaultStorable("Item3", StorableType.BEVERAGE, LocalDate.parse("2022-10-11"));
        DefaultStorable d = new DefaultStorable("Item4", StorableType.BEVERAGE, LocalDate.parse("2022-11-07"));
        DefaultStorable e = new DefaultStorable("Item5", StorableType.BEVERAGE, LocalDate.parse("2021-12-29"));

        List<DefaultStorable> l = new ArrayList<>();
        l.add(a);
        l.add(b);
        l.add(c);
        l.add(d);
        l.add(e);

        System.out.println(l);

        Collections.sort(l, new ItemExpirationDecreasingComparator());

        System.out.println(l);


    }

}
