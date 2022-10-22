package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

public class Hotel extends BookableAbstract{

    private static final String identifier = "HOT-";
    private static int counterOfInstances = -1;


    public Hotel(Location location, double pricePerNight){
        super(location, pricePerNight);
        this.id = ++counterOfInstances;
    }

    /**
     * @return the unique ID of the Bookable.
     * It is String with prefix the first three letters (all-caps) of the accommodation type,
     * a dash and a sequential number counting the number of created accommodation instances of the respective type:
     * e.g. "HOT-45", "APA-12", "VIL-7". Note that counting starts from 0 and is done separately for the different types.
     */
    @Override
    public String getId() {
        return new StringBuilder(Hotel.identifier + this.id).toString();
    }

    public static void main(String[] args) {
        Location l = new Location(6,6);

        Hotel h = new Hotel(l, 60);

        Hotel t = new Hotel(l, 60);

        System.out.println(h.getId());
        System.out.println(t.getId());
    }

}
