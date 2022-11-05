package bg.sofia.uni.fmi.mjt.flightscanner.comparators;

import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;

import java.util.Comparator;

public class FlightByFreeSeatsComparator implements Comparator<Flight> {

    @Override
    public int compare(Flight first, Flight second) {

        return Integer.compare(second.getFreeSeatsCount(), first.getFreeSeatsCount());
    }
}
