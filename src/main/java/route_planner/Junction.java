package route_planner;

import graphs.Identifiable;

import java.io.PrintStream;
import java.util.Locale;

public class Junction implements Identifiable {
    private String name;            // unique name of the junction
    private double locationX;       // RD x-coordinate in km
    private double locationY;       // RD y-coordinate in km
    private String province;
    private int population;

    /**
     * Constructor that accepts a Builder object
     * This is part of the Builder pattern - creates Junction from builder's values
     */
    public Junction(Builder builder) {
        this.name = builder.name;
        this.locationX = builder.locationX;
        this.locationY = builder.locationY;
        this.province = builder.province;
        this.population = builder.population;
    }

    public Junction(String name) {
        this.name = name;
    }

    /**
     * Equals method defines when two junctions are considered the same
     * Two junctions are equal if they have the same name
     * This is crucial for HashMap operations O(1) lookups depend on proper equals
     */
    @Override
    public boolean equals(Object obj) {
        // Check if same object reference - O(1)
        if (this == obj) return true; //"This" refers to the object being called and obj refers to the provided object

        // Check for null or different class - O(1)
        if (obj == null || getClass() != obj.getClass()) return false;

        //Cast the object to junction because in here we are sure its a junction because i checked the class in line above
        Junction junction = (Junction) obj;
        return name.equals(junction.name); //If its the same name return true
    }

    /**
     * HashCode method - returns a hash value for this junction
     * MUST be consistent with equals: equal objects must have equal hashcodes
     * This enables O(1) average time complexity in HashMap operations
     */
    @Override
    public int hashCode() {
        // Use name's hashcode since name is our equality criteria
        return name.hashCode();
    }

    // -----------------------------
    // Public getters and setters
    // -----------------------------

    /**
     * Implementation of Identifiable interface
     * Returns the unique identifier for this junction
     * This allows the DirectedGraph to store junctions by their ID
     * @return the name as the unique identifier
     */
    @Override
    public String getId() {
        return name;  // Name serves as unique ID (no two cities have same name)
    }

    public String getName() {
        return name;
    }

    public double getLocationX() {
        return locationX;
    }

    public double getLocationY() {
        return locationY;
    }

    public String getProvince() {
        return province;
    }

    public int getPopulation() {
        return population;
    }


    // -----------------------------
    // Utility methods
    // -----------------------------

    /**
     * calculates the carthesion distance between two junctions
     *
     * @param target
     * @return
     */
    double getDistance(Junction target) {
        // Calculate the cartesion distance between this and the target junction
        // using the locationX and locationY as provided in the dutch RD-coordinate system
        double dX = target.locationX - locationX;
        double dY = target.locationY - locationY;
        return Math.sqrt(dX * dX + dY * dY);
    }


    /**
     * draws the junction onto a svg image with a given colour
     *
     * @param svgWriter
     * @param colour
     */
    public void svgDraw(PrintStream svgWriter, String colour) {
        // calculate the size of the dot relative to population at the junction
        double radius = 0.1 + 0.3 * Math.log(1 + population / 2000);
        //radius = 0.1;
        int fontSize = 3;

        // accounts for the reversed y-direction of the svg coordinate system relative to RD-coordinates
        svgWriter.printf(Locale.ENGLISH, "<circle cx='%.3f' cy='%.3f' r='%.3f' fill='%s'/>\n",
                locationX, -locationY, radius, colour);
        svgWriter.printf(Locale.ENGLISH, "<text x='%.3f' y='%.3f' font-size='%d' fill='%s' text-anchor='middle'>%s</text>\n",
                locationX, -locationY - 1.3, fontSize, colour, name);

    }

    /**
     * Draws the road segment onto a .svg image with the specified colour
     * If no colour is provided, a default will be calculated on the basis of the maxSpeed
     *
     * @param svgWriter
     * @param colour
     */
    public void svgDrawRoad(PrintStream svgWriter, Junction from, double width, String colour) {
        if (from == null) return;
        // accounts for the reversed y-direction of the svg coordinate system relative from RD-coordinates
        svgWriter.printf(Locale.ENGLISH, "<line x1='%.3f' y1='%.3f' x2='%.3f' y2='%.3f' stroke-width='%.3f' stroke='%s'/>\n",
                this.getLocationX(), -this.getLocationY(),
                from.getLocationX(), -from.getLocationY(),
                width, colour);
    }

    /**
     * Builder class - implements the Builder pattern
     * Allows optional parameters and improves readablity
     */
    public static class Builder {
        // Builder's own copies of Junction fields
        private String name;
        private double locationX = 0.0;      // Default coordinates
        private double locationY = 0.0;
        private String province = null;      // Optional
        private int population = 0;          // Default population

        /**
         * Sets the name and returns Builder for method chaining
         * Pattern: builder.name("Amsterdam").population(850000).build()
         */
        public Builder name(String name) {
            this.name = name;
            return this;  // Return 'this' enables method chaining
        }

        /**
         * Sets both X and Y coordinates at once
         * More convenient than setting them separately
         */
        public Builder location(double x, double y) {
            this.locationX = x;
            this.locationY = y;
            return this;
        }

        /**
         * Sets the province name
         */
        public Builder province(String province) {
            this.province = province;
            return this;
        }

        /**
         * Sets the population
         */
        public Builder population(int population) {
            this.population = population;
            return this;
        }

        /**
         * Creates the actual Junction object with all collected values
         * This is the final step in the builder chain
         */
        public Junction build() {
            return new Junction(this);  // Pass builder to Junction constructor
        }
    }

    // -----------------------------
    // Object overrides
    // -----------------------------

    @Override
    public String toString() {
        return name;
    }

}
