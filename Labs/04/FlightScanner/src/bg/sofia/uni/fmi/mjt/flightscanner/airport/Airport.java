package bg.sofia.uni.fmi.mjt.flightscanner.airport;

import java.util.Objects;

public record Airport(String ID) {

    public Airport {

        if (ID == null || ID.isBlank() || ID.isEmpty()) {
            throw new IllegalArgumentException("ID is null, empty or blank.");
        }
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Airport airport = (Airport) o;
        return Objects.equals(ID, airport.ID);
    }

    @Override
    public int hashCode() {

        return Objects.hash(ID);
    }

    @Override
    public String toString() {

        return "Airport{" +
            "ID='" + ID + '\'' +
            '}';
    }
}
