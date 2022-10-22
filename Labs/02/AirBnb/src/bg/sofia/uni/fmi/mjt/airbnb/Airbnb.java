package bg.sofia.uni.fmi.mjt.airbnb;

import bg.sofia.uni.fmi.mjt.airbnb.accommodation.Bookable;
import bg.sofia.uni.fmi.mjt.airbnb.filter.Criterion;

import java.util.Arrays;

public class Airbnb implements AirbnbAPI{

    private final Bookable[] accommodations;

    public Airbnb(Bookable[] accommodations){
            this.accommodations=accommodations;
    }

    /**
     * Finds an accommodation by ID.
     *
     * @param id the unique ID of the bookable
     * @return the bookable with the specified id (the IDs are case-insensitive).
     * If the id is null or blank, or no accommodation with the specified id is found, return null.
     */
    @Override
    public Bookable findAccommodationById(String id) {
        if(id == null || id.isBlank()){
            return null;
        }

        for(Bookable bookable : this.accommodations){
            if (bookable.getId().equalsIgnoreCase(id)){
                return bookable;
            }
        }
        return null;
    }

    /**
     * Estimates the total revenue of all booked accommodations.
     *
     * @return the sum of total prices of stay for all booked accommodations.
     */
    @Override
    public double estimateTotalRevenue() {
        double totalSum = 0;
        for(Bookable bookable : this.accommodations){
            totalSum += bookable.getTotalPriceOfStay();
        }
        return totalSum;
    }

    /**
     * @return the number of booked accommodations.
     */
    @Override
    public long countBookings() {
        long counterBookings = 0;
        for(Bookable bookable : this.accommodations){
            if(bookable.isBooked() == true){
                counterBookings++;
            }
        }
        return counterBookings;
    }

    /**
     * Filters the accommodations by given criteria
     *
     * @param criteria the criteria to apply
     * @return an array of accommodations matching the specified criteria
     */
    @Override
    public Bookable[] filterAccommodations(Criterion... criteria) {
        Bookable[] fulfilBookable = new Bookable[this.accommodations.length];
        int counter = 0;

        for (Bookable bookable : this.accommodations){

            boolean fulfil = true;

            for(Criterion currentCriteria : criteria){
                if(currentCriteria.check(bookable) == false){
                    fulfil = false;
                }
                break;
            }

            if (fulfil == true){
                fulfilBookable[counter++] = bookable;
            }

        }

        return Arrays.copyOf(fulfilBookable, counter);

       // Bookable[] resultBookable = Arrays.copyOf(fulfilBookable, counter);

        /*Bookable[] resultBookable = new Bookable[counter];

        for(int i = 0; i < counter; i++){
            resultBookable[i] = fulfilBookable[i];
        }*/

       // return resultBookable;
    }
}
