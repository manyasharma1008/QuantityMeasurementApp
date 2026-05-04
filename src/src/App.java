public interface IMeasurable {
    double getConversionFactor();
    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double baseValue);
    String getUnitName();
}

public enum LengthUnit implements IMeasurable {
    FEET(12.0), INCHES(1.0), YARDS(36.0), CENTIMETERS(0.3937);

    private final double conversionFactor;

    LengthUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    public String getUnitName() {
        return name();
    }
}

public enum WeightUnit implements IMeasurable {
    KILOGRAM(1000.0), GRAM(1.0), POUND(453.592);

    private final double conversionFactor;

    WeightUnit(double conversionFactor) {
        this.conversionFactor = conversionFactor;
    }

    public double getConversionFactor() {
        return conversionFactor;
    }

    public double convertToBaseUnit(double value) {
        return value * conversionFactor;
    }

    public double convertFromBaseUnit(double baseValue) {
        return baseValue / conversionFactor;
    }

    public String getUnitName() {
        return name();
    }
}

public final class Quantity<U extends IMeasurable> {
    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        if (unit == null || !Double.isFinite(value)) {
            throw new IllegalArgumentException("Invalid value or unit");
        }
        this.value = value;
        this.unit = unit;
    }

    public Quantity<U> convertTo(U targetUnit) {
        double baseValue = unit.convertToBaseUnit(value);
        double converted = targetUnit.convertFromBaseUnit(baseValue);
        return new Quantity<>(Math.round(converted * 100.0) / 100.0, targetUnit);
    }

    public Quantity<U> add(Quantity<U> other) {
        double baseSum = unit.convertToBaseUnit(value) + other.unit.convertToBaseUnit(other.value);
        double result = unit.convertFromBaseUnit(baseSum);
        return new Quantity<>(Math.round(result * 100.0) / 100.0, unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        double baseSum = unit.convertToBaseUnit(value) + other.unit.convertToBaseUnit(other.value);
        double result = targetUnit.convertFromBaseUnit(baseSum);
        return new Quantity<>(Math.round(result * 100.0) / 100.0, targetUnit);
    }

    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Quantity<?> other)) return false;
        if (!unit.getClass().equals(other.unit.getClass())) return false;
        double thisBase = unit.convertToBaseUnit(value);
        double otherBase = ((U) other.unit).convertToBaseUnit(other.value);
        return Double.compare(thisBase, otherBase) == 0;
    }

    public int hashCode() {
        return Double.hashCode(unit.convertToBaseUnit(value));
    }

    public String toString() {
        return "Quantity(" + value + ", " + unit.getUnitName() + ")";
    }
}

public class App {
    public static void main(String[] args) {
        Quantity<LengthUnit> q1 = new Quantity<>(1.0, LengthUnit.FEET);
        Quantity<LengthUnit> q2 = new Quantity<>(12.0, LengthUnit.INCHES);
        System.out.println(q1.equals(q2));
        System.out.println(q1.convertTo(LengthUnit.INCHES));
        System.out.println(q1.add(q2, LengthUnit.FEET));

        Quantity<WeightUnit> w1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
        Quantity<WeightUnit> w2 = new Quantity<>(1000.0, WeightUnit.GRAM);
        System.out.println(w1.equals(w2));
        System.out.println(w1.convertTo(WeightUnit.GRAM));
        System.out.println(w1.add(w2, WeightUnit.KILOGRAM));

        System.out.println(q1.equals(w1));
    }
}
