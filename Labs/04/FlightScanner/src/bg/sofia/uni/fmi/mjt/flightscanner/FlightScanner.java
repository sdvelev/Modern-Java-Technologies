package bg.sofia.uni.fmi.mjt.flightscanner;

import bg.sofia.uni.fmi.mjt.flightscanner.airport.Airport;
import bg.sofia.uni.fmi.mjt.flightscanner.comparators.FlightByDestinationComparator;
import bg.sofia.uni.fmi.mjt.flightscanner.comparators.FlightByFreeSeatsComparator;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.Flight;
import bg.sofia.uni.fmi.mjt.flightscanner.flight.RegularFlight;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FlightScanner implements FlightScannerAPI {

    private final Map<Airport, HashSet<Flight>> adjacencyList;

    public FlightScanner() {

        this.adjacencyList = new HashMap<>();
    }

    /**
     * Adds a flight. If the same flight has already been added, call does nothing.
     *
     * @param flight the flight to add.
     * @throws IllegalArgumentException if flight is null.
     */
    @Override
    public void add(Flight flight) {

        validateFlightNull(flight);

        this.adjacencyList.putIfAbsent(flight.getFrom(), new HashSet<>());
        this.adjacencyList.putIfAbsent(flight.getTo(), new HashSet<>());
        this.adjacencyList.get(flight.getFrom()).add(flight);
    }

    /**
     * Adds all flights specified. If any of the flights have already been added, those are ignored.
     *
     * @param flights the flights to be added.
     * @throws IllegalArgumentException if flights is null.
     */
    @Override
    public void addAll(Collection<Flight> flights) {

        validateFlightsNull(flights);

        for (Flight flight : flights) {

            this.add(flight);
        }
    }

    /**
     * Returns a list of flights from start to destination airports with minimum number of transfers.
     * If there are several such flights with equal minimum number of transfers, returns any of them.
     * If the destination is not reachable with any sequence of flights from the start airport, returns an empty list.
     *
     * @param from the start airport.
     * @param to   the destination airport.
     * @throws IllegalArgumentException if from or to is null, or if from and to are one and the same airport.
     */

    @Override
    public List<Flight> searchFlights(Airport from, Airport to) {

        validateAirportNull(from);

        validateAirportNull(to);

        validateAirportsSame(from, to);

        Queue<Airport> waitingQueue = new ArrayDeque<>();
        Set<Airport> visited = new HashSet<>();
        Map<Airport, Flight> parentOf = new HashMap<>();

        //Prerequisite for BFS
        visited.add(from);
        parentOf.put(from, null);
        waitingQueue.add(from);

        boolean found = bfsFlights(waitingQueue, visited, parentOf, to);

        List<Flight> pathFinal = new ArrayList<>();

        if (!found) {
            return pathFinal;
        }

        extractPath(to, pathFinal, parentOf);

        Collections.reverse(pathFinal);
        return pathFinal;
    }

    /**
     * Gets an unmodifiable copy of all outbound flights from the specified airport,
     * sorted by number of free seats, in descending order.
     *
     * @param from the airport.
     * @throws IllegalArgumentException if from is null.
     */
    @Override
    public List<Flight> getFlightsSortedByFreeSeats(Airport from) {

        validateAirportNull(from);

        List<Flight> searchedFlights = new ArrayList<>(this.adjacencyList.get(from));
        Collections.sort(searchedFlights, new FlightByFreeSeatsComparator());
        return List.copyOf(searchedFlights);
    }

    /**
     * Gets an unmodifiable copy of all outbound flights from the specified airport,
     * sorted by destination airport ID, in lexicographic order.
     *
     * @param from the airport.
     * @throws IllegalArgumentException if from is null.
     */
    @Override
    public List<Flight> getFlightsSortedByDestination(Airport from) {

        validateAirportNull(from);

        List<Flight> searchedFlights = new ArrayList<>(this.adjacencyList.get(from));
        Collections.sort(searchedFlights, new FlightByDestinationComparator());
        return List.copyOf(searchedFlights);
    }

    public static void main(String[] args) {

        FlightScanner general = new FlightScanner();

        //Airports
        Airport sof = new Airport("SOF");
        Airport bbu = new Airport("BBU");
        Airport dbx = new Airport("DBX");
        Airport vie = new Airport("VIE");
        Airport stn = new Airport("STN");
        Airport lhr = new Airport("LHR");
        Airport lon = new Airport("LON");
        Airport jfk = new Airport("JFK");

        // Flights
        general.add(RegularFlight.of("WIZ81", sof, vie, 100));
        general.add(RegularFlight.of("AA16", sof, vie, 100));
        general.add(RegularFlight.of("LZ13", sof, vie, 100));
        general.add(RegularFlight.of("LZ160", bbu, sof, 100));
        general.add(RegularFlight.of("BA185", stn, sof, 100));
        general.add(RegularFlight.of("WIZ15", stn, sof, 100));
        general.add(RegularFlight.of("AA05", vie, stn, 100));
        general.add(RegularFlight.of("AA151", stn, vie, 100));
        general.add(RegularFlight.of("BA17", lhr, stn, 100));
        general.add(RegularFlight.of("BA7", stn, lhr, 100));
        general.add(RegularFlight.of("AF88", lhr, lon, 100));
        general.add(RegularFlight.of("BA101", lhr, jfk, 100));
        general.add(RegularFlight.of("UA173", jfk, dbx, 100));

        System.out.println(general.searchFlights(vie, sof));

    }

    private static void validateFlightNull(Flight flight) {

        if (flight == null) {
            throw new IllegalArgumentException("flight is null.");
        }
    }

    private static void validateFlightsNull(Collection<Flight> flights) {

        if (flights == null) {
            throw new IllegalArgumentException("flights is null.");
        }
    }

    private static void validateAirportNull(Airport airport) {

        if (airport == null) {
            throw new IllegalArgumentException("airport is null.");
        }
    }

    private static void validateAirportsSame(Airport from, Airport to) {

        if (from.equals(to)) {
            throw new IllegalArgumentException("Airports from and to are the same.");
        }
    }

    private boolean bfsFlights(Queue<Airport> waitingQueue, Set<Airport> visited,
                               Map<Airport, Flight> parentOf, Airport to) {

        boolean found = false;

        while (!waitingQueue.isEmpty()) {

            Airport current = waitingQueue.element();
            waitingQueue.remove();

            if (current.equals(to)) {

                found = true;
                break;
            }

            for (Flight neighbour : this.adjacencyList.get(current)) {

                Airport destination = neighbour.getTo();

                if (!visited.contains(destination)) {
                    visited.add(destination);
                    waitingQueue.add(destination);
                    parentOf.put(destination, neighbour);
                }
            }
        }

        return found;
    }

    private static void extractPath(Airport to, List<Flight> pathFinal, Map<Airport, Flight> parentOf) {

        Airport currentAirport = to;

        while (currentAirport != null) {

            Flight previousFlight = parentOf.get(currentAirport);

            if (previousFlight == null) {
                break;
            }

            pathFinal.add(previousFlight);
            currentAirport = previousFlight.getFrom();
        }
    }

}
