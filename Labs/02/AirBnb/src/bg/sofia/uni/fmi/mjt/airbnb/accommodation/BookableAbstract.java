package bg.sofia.uni.fmi.mjt.airbnb.accommodation;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.location.Location;

import java.time.Duration;
import java.time.LocalDateTime;


public abstract class BookableAbstract implements Bookable {

    private final Location location;
    private final double pricePerNight;
    private boolean isBooked;
    private long nightsCount;

    public BookableAbstract(Location location, double pricePerNight){
        this.location=location;
        this.pricePerNight=pricePerNight;
        this.isBooked = false;
        this.nightsCount = 0;
    }


    /**
     * @return the location of the Bookable.
     */
    @Override
    public Location getLocation() {
        return this.location;
    }

    /**
     * Checks if the accommodation is booked.
     *
     * @return true, if the accommodation is booked.
     */
    @Override
    public boolean isBooked() {
        return this.isBooked;
    }

    /**
     * Books the accommodation for the specified date interval.
     *
     * @param checkIn  check-in date
     * @param checkOut check-out date
     * @return true, if the accommodation is booked successfully.
     * If the accommodation has been previously booked, does nothing and returns false.
     * If checkIn or checkOut is null, or checkIn is in the past, or checkIn is not strictly before checkOut, returns false
     */
    @Override
    public boolean book(LocalDateTime checkIn, LocalDateTime checkOut) {
        if (this.isBooked == true){
            return false;
        }

        if (checkIn == null || checkOut == null || checkIn.isBefore(LocalDateTime.now()) || checkOut.isBefore(checkIn)){
            return false;
        }

        this.isBooked = true;
        this.nightsCount = Duration.between(checkIn, checkOut).toDays();
        return true;
    }

    /**
     * @return If the accommodation is booked, returns the total price of the stay: the number of nights of the booking,
     * multiplied by the price per night. If the accommodation is not booked, returns 0.0.
     */
    @Override
    public double getTotalPriceOfStay() {

        if(this.isBooked == false){
            return 0;
        }

        return this.nightsCount * this.pricePerNight;
    }

    /**
     * @return The price per night for this accommodation.
     */
    @Override
    public double getPricePerNight() {
        return this.pricePerNight;
    }
}
