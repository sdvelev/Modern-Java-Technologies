package bg.sofia.uni.fmi.mjt.flightscanner.comparators;

import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;

import java.util.Comparator;

public class FlightByDestinationComparator implements Comparator<Flight> {

    @Override
    public int compare(Flight first, Flight second) {

        return first.getTo().ID().compareTo(second.getTo().ID());
    }
}
