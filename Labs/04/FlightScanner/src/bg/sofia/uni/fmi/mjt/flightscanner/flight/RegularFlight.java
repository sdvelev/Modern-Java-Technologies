package bg.sofia.uni.fmi.mjt.flightscanner.flight;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.FlightCapacityExceededException;
import bg.sofia.uni.fmi.mjt.flightscanner.exception.InvalidFlightException;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Gender;
import bg.sofia.uni.fmi.mjt.flightscanner.passenger.Passenger;

import java.util.*;

public class RegularFlight implements Flight {

    final private String flightId;
    final private Airport from;
    final private Airport to;
    final private int totalCapacity;
    final private Set<Passenger> passengers;

    public static RegularFlight of(String flightId, Airport from, Airport to, int totalCapacity)
        throws InvalidFlightException {

        validateFlightId(flightId);

        validateAirportsNull(from, to);

        validateTotalCapacity(totalCapacity);

        validateAirportsSame(from, to);

        return new RegularFlight(flightId, from, to, totalCapacity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegularFlight that = (RegularFlight) o;
        return Objects.equals(flightId, that.flightId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(flightId);
    }

    /**
     * Gets the start airport of this flight.
     */
    @Override
    public Airport getFrom() {

        return this.from;
    }

    /**
     * Gets the destination airport of this flight.
     */
    @Override
    public Airport getTo() {

        return this.to;
    }

    /**
     * Adds a passenger to this flight.
     *
     * @param passenger the passenger to add.
     * @throws FlightCapacityExceededException, in case the flight is already fully booked (there are no free seats).
     */
    @Override
    public void addPassenger(Passenger passenger) throws FlightCapacityExceededException {

        if (this.passengers.size() == this.totalCapacity) {
            throw new FlightCapacityExceededException("The flight is already fully booked (there are no free seats).");
        }

        this.passengers.add(passenger);
    }

    /**
     * Adds a collection of passengers to this flight.
     *
     * @param passengers the passengers to add.
     * @throws FlightCapacityExceededException, in case the flight cannot accommodate that many passengers
     *                                          (the available free seats are not enough).
     */
    @Override
    public void addPassengers(Collection<Passenger> passengers) throws FlightCapacityExceededException {

        if (this.passengers.size() + passengers.size() > this.totalCapacity) {
            throw new FlightCapacityExceededException("The flight cannot accommodate that many passengers " +
                "(the available free seats are not enough).");
        }

        this.passengers.addAll(passengers);
    }

    /**
     * Gets all passengers on this flight. If there are no passengers, returns an empty list.
     *
     * @return a collection of all passengers on this flight, as an unmodifiable copy.
     */
    @Override
    public Collection<Passenger> getAllPassengers() {

        if (this.passengers.size() == 0) {
            return this.passengers;
        }

        return Set.copyOf(this.passengers);
    }

    /**
     * Gets the number of free seats on this flight.
     */
    @Override
    public int getFreeSeatsCount() {

        return this.totalCapacity - this.passengers.size();
    }

    public static void main(String[] args) throws FlightCapacityExceededException {
        Passenger a = new Passenger("ID1", "A", Gender.MALE);
        Passenger b = new Passenger("ID2", "B", Gender.FEMALE);
        Passenger c = new Passenger("ID3", "C", Gender.MALE);
        Passenger d = new Passenger("ID4", "D", Gender.FEMALE);
        Passenger e = new Passenger("ID5", "E", Gender.MALE);

        Airport x = new Airport("SOF");
        Airport y = new Airport("LON");

        RegularFlight reg = RegularFlight.of("BAW", x, y, 10);

       // reg.addPassengers(List.of(a, b, c));
        System.out.println(reg.getFreeSeatsCount());

        Collection<Passenger> ls = reg.getAllPassengers();
        System.out.println(ls);

        //ls.remove(a);
    }

    private RegularFlight(String flightId, Airport from, Airport to, int totalCapacity) {
        this.flightId = flightId;
        this.from = from;
        this.to = to;
        this.totalCapacity = totalCapacity;
        this.passengers = new HashSet<>();
    }

    private static void validateFlightId(String flightId) {

        if (flightId == null || flightId.isEmpty() || flightId.isBlank()) {
            throw new IllegalArgumentException("flightId is null, empty or blank.");
        }
    }

    private static void validateAirportsNull(Airport from, Airport to) {

        if (from == null || to == null) {
            throw new IllegalArgumentException("from and to airports are null.");
        }
    }

    private static void validateTotalCapacity(int totalCapacity) {

        if (totalCapacity < 0) {
            throw new IllegalArgumentException("Total capacity is a negative number.");
        }
    }

    private static void validateAirportsSame(Airport from, Airport to) {

        if (from.equals(to)) {
            throw new InvalidFlightException("from and to airports are the same airport.");
        }
    }

    @Override
    public String toString() {
        return "RegularFlight{" +
            "flightId='" + flightId + '\'' +
            ", from=" + from +
            ", to=" + to +
            ", totalCapacity=" + totalCapacity +
            ", passengers=" + passengers +
            '}';
    }
}
