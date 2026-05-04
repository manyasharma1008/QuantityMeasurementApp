import java.util.Objects;

public class UC1 {
// QuantityMeasurementApp.java
import java.util.*;

    /**
     * Quantity Measurement Application - Comprehensive implementation
     * covering UC1 through UC9 requirements
     */
    public class QuantityMeasurementApp {

        // ==================== UC1: Feet Measurement Equality ====================

        /**
         * Feet class for UC1 - Feet measurement equality
         */
        public static class Feet {
            private final double value;

            public Feet(double value) {
                if (value < 0) {
                    throw new IllegalArgumentException("Feet value cannot be negative");
                }
                this.value = value;
            }

            public double getValue() {
                return value;
            }

            /**
             * Checks equality with another Feet object
             * @param other the other Feet object to compare with
             * @return true if values are equal within tolerance
             */
            public boolean equals(Feet other) {
                if (other == null) return false;
                // Using epsilon comparison for floating point numbers
                final double EPSILON = 1e-9;
                return Math.abs(this.value - other.value) < EPSILON;
            }

            @Override
            public String toString() {
                return value + " feet";
            }
        }

        // ==================== UC2: Feet and Inches measurement equality ====================

        /**
         * Inches class for UC2 - Inches measurement equality
         */
        public static class Inches {
            private final double value;

            public Inches(double value) {
                if (value < 0) {
                    throw new IllegalArgumentException("Inches value cannot be negative");
                }
                this.value = value;
            }

            public double getValue() {
                return value;
            }

            /**
             * Checks equality with another Inches object
             * @param other the other Inches object to compare with
             * @return true if values are equal within tolerance
             */
            public boolean equals(Inches other) {
                if (other == null) return false;
                final double EPSILON = 1e-9;
                return Math.abs(this.value - other.value) < EPSILON;
            }

            @Override
            public String toString() {
                return value + " inches";
            }
        }

        // ==================== UC3: Generic Quantity Class for DRY Principle ====================

        /**
         * Generic Quantity class for UC3 - DRY principle implementation
         * @param <T> the type of the quantity value
         */
        public static class GenericQuantity<T> {
            private final T value;
            private final String unit;

            public GenericQuantity(T value, String unit) {
                this.value = value;
                this.unit = unit;
            }

            public T getValue() {
                return value;
            }

            public String getUnit() {
                return unit;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) return true;
                if (obj == null || getClass() != obj.getClass()) return false;

                GenericQuantity<?> that = (GenericQuantity<?>) obj;
                return Objects.equals(value, that.value) &&
                        Objects.equals(unit, that.unit);
            }

            @Override
            public int hashCode() {
                return Objects.hash(value, unit);
            }

            @Override
            public String toString() {
                return value + " " + unit;
            }
        }

        // ==================== UC4 & UC8: Extended Unit Support (Standalone Enum) ====================

        /**
         * Unit enum for UC4 and UC8 - Extended unit support
         * Refactored as standalone enum as required in UC8
         */
        public enum Unit {
            FEET("feet", 12.0),      // 1 foot = 12 inches
            INCHES("inches", 1.0),   // 1 inch = 1 inch
            YARDS("yards", 36.0),    // 1 yard = 36 inches
            MILES("miles", 63360.0), // 1 mile = 63360 inches
            CENT
}
