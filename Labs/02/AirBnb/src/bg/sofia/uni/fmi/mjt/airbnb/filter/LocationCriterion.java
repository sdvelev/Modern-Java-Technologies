package bg.sofia.uni.fmi.mjt.airbnb.filter;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class LocationCriterion implements Criterion{

    private final Location currentLocation;
    private final double maxDistance;

    public LocationCriterion(Location currentLocation, double maxDistance){
        this.currentLocation = currentLocation;
        this.maxDistance = maxDistance;
    }

    /**
     * @param bookable
     * @return true, if the bookable matches the criterion. If bookable is null, returns false.
     */
    @Override
    public boolean check(Bookable bookable) {
        if(bookable == null){
            return false;
        }

        if(Math.sqrt(Math.pow((Math.abs(this.currentLocation.getX()-bookable.getLocation().getX())),2) +
                Math.pow((Math.abs(this.currentLocation.getY()-bookable.getLocation().getY())),2)) <= this.maxDistance){
            return true;
        }
        return false;
    }
}
