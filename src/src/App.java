public class App {

    enum LengthUnit {
        FEET(1.0),
        INCH(1.0 / 12.0),
        YARD(3.0),
        CM(0.0328084); // 1 cm in feet

        private final double toFeetFactor;

        LengthUnit(double toFeetFactor) {
            this.toFeetFactor = toFeetFactor;
        }

        public double toFeet(double value) {
            return value * toFeetFactor;
        }
    }

    static class Length {
        private final double value;
        private final LengthUnit unit;
        private static final double EPSILON = 1e-6;

        public Length(double value, LengthUnit unit) {
            if (unit == null) {
                throw new IllegalArgumentException("Unit cannot be null");
            }
            this.value = value;
            this.unit = unit;
        }

        private double toFeet() {
            return unit.toFeet(value);
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || this.getClass() != obj.getClass()) return false;

            Length other = (Length) obj;
            return Math.abs(this.toFeet() - other.toFeet()) < EPSILON;
        }

        @Override
        public int hashCode() {
            long temp = Double.doubleToLongBits(toFeet());
            return (int) (temp ^ (temp >>> 32));
        }
    }

    public static boolean compare(Length l1, Length l2) {
        if (l1 == null || l2 == null) return false;
        return l1.equals(l2);
    }

    public static void main(String[] args) {

        System.out.println("1 Yard == 3 Feet: " +
                compare(new Length(1.0, LengthUnit.YARD),
                        new Length(3.0, LengthUnit.FEET)));

        System.out.println("1 Yard == 36 Inches: " +
                compare(new Length(1.0, LengthUnit.YARD),
                        new Length(36.0, LengthUnit.INCH)));

        System.out.println("2 Yard == 2 Yard: " +
                compare(new Length(2.0, LengthUnit.YARD),
                        new Length(2.0, LengthUnit.YARD)));

        System.out.println("2 cm == 2 cm: " +
                compare(new Length(2.0, LengthUnit.CM),
                        new Length(2.0, LengthUnit.CM)));

        System.out.println("1 cm == 0.393701 inch: " +
                compare(new Length(1.0, LengthUnit.CM),
                        new Length(0.393701, LengthUnit.INCH)));

        System.out.println("1 cm != 1 feet: " +
                compare(new Length(1.0, LengthUnit.CM),
                        new Length(1.0, LengthUnit.FEET)));

        Length yard = new Length(1.0, LengthUnit.YARD);
        Length feet = new Length(3.0, LengthUnit.FEET);
        Length inch = new Length(36.0, LengthUnit.INCH);

        System.out.println("Transitive (yard == feet == inch): " +
                (compare(yard, feet) && compare(feet, inch) && compare(yard, inch)));

        Length l = new Length(1.0, LengthUnit.FEET);
        System.out.println("Null check: " + l.equals(null));
        System.out.println("Same reference: " + l.equals(l));
    }
}