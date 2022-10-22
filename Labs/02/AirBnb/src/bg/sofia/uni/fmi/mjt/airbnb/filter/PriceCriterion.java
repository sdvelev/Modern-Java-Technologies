package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;

public class PriceCriterion implements Criterion{

    private final double minPrice;
    private final double maxPrice;

    public PriceCriterion(double minPrice, double maxPrice){
        this.minPrice=minPrice;
        this.maxPrice=maxPrice;
    }

    /**
     * @param bookable
     * @return true, if the bookable matches the criterion. If bookable is null, returns false.
     */
    @Override
    public boolean check(Bookable bookable) {
        if (bookable == null){
            return false;
        }

        if (bookable.getPricePerNight() >= minPrice && bookable.getPricePerNight() <= maxPrice){
            return true;
        }
        return false;
    }
}
