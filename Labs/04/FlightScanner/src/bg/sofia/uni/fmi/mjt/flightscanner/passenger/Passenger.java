package bg.sofia.uni.fmi.mjt.flightscanner.passenger;

import java.util.Objects;

public record Passenger(String id, String name, Gender gender) {

    public Passenger {

        if (id == null || id.isBlank() || id.isEmpty()) {
            throw new IllegalArgumentException("id is null, empty or blank.");
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return Objects.equals(id, passenger.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Passenger{" +
            "id='" + id + '\'' +
            ", name='" + name + '\'' +
            ", gender=" + gender +
            '}';
    }

}
