package bg.sofia.uni.fmi.mjt.mail;

import java.util.Objects;

public record Account(String emailAddress, String name) {

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return name.equals(account.name);
    }

    @Override
    public int hashCode() {

        return Objects.hash(name);
    }
}