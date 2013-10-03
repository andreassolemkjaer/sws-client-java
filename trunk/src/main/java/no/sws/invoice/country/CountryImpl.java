package no.sws.invoice.country;

/**
 * User: Adam
 * Date: 10/3/13
 * Time: 8:47 AM
 */
public class CountryImpl implements Country {
    private final String name;

    public CountryImpl(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CountryImpl country = (CountryImpl) o;

        if (name != null ? !name.equals(country.name) : country.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "CountryImpl{" +
                "name='" + name + '\'' +
                '}';
    }
}
