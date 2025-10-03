package graphs;

public class Country implements Identifiable {

    private String name;
    private int population;

    public Country(String name) {
        this.name = name;
    }

    public Country(String name, int population) {
        this(name);
        this.population = population;
    }

    @Override
    public String getId() {
        return name;
    }

    public int getPopulation() {
        return population;
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return this.name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        Country country = (Country) o;
        return name.equals(country.name);
    }
}
