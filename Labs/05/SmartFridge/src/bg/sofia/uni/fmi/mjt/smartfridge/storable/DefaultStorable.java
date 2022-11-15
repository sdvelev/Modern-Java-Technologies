package bg.sofia.uni.fmi.mjt.smartfridge.storable;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.type.StorableType;

import java.time.LocalDate;
import java.util.Objects;


public class DefaultStorable implements Storable {

    final private String name;
    final private StorableType type;
    final private LocalDate expiration;

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

        return this.expiration.isBefore(LocalDate.now()) /*|| this.expiration.isEqual(LocalDate.now())*/;
    }

    @Override
    public String toString() {
        return "DefaultStorable{" +
            "name='" + name + '\'' +
            ", type=" + type +
            ", expiration=" + expiration +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DefaultStorable that = (DefaultStorable) o;
        return name.equals(that.name) && type == that.type && expiration.equals(that.expiration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, expiration);
    }

}
