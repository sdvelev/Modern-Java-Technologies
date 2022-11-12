package bg.sofia.uni.fmi.mjt.smartfridge.comparators;

import bg.sofia.uni.fmi.mjt.smartfridge.storable.Storable;

import java.util.Comparator;

public class ItemExpirationDecreasingComparator implements Comparator<Storable> {

    @Override
    public int compare(Storable first, Storable second) {

        if (first.getExpiration().isAfter(second.getExpiration())) {
            return 1;
        }
        else if (first.getExpiration().isBefore(second.getExpiration())) {
            return -1;
        }

        return 0;
    }
}
