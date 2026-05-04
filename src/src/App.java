import java.util.function.DoubleBinaryOperator;

public class Quantity<U extends IMeasurable<U>> {

    private final double value;
    private final U unit;

    public Quantity(double value, U unit) {
        this.value = value;
        this.unit = unit;
    }

    private enum ArithmeticOperation {
        ADD((a, b) -> a + b),
        SUBTRACT((a, b) -> a - b),
        DIVIDE((a, b) -> {
            if (b == 0) throw new ArithmeticException("Division by zero");
            return a / b;
        });

        private final DoubleBinaryOperator operation;

        ArithmeticOperation(DoubleBinaryOperator operation) {
            this.operation = operation;
        }

        double compute(double a, double b) {
            return operation.applyAsDouble(a, b);
        }
    }

    private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetRequired) {
        if (other == null) throw new IllegalArgumentException("Operand cannot be null");
        if (this.unit == null || other.unit == null)
            throw new IllegalArgumentException("Unit cannot be null");
        if (!this.unit.getClass().equals(other.unit.getClass()))
            throw new IllegalArgumentException("Incompatible unit types");
        if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
            throw new IllegalArgumentException("Values must be finite");
        if (targetRequired && targetUnit == null)
            throw new IllegalArgumentException("Target unit cannot be null");
    }

    private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {
        double baseThis = unit.toBaseUnit(value);
        double baseOther = other.unit.toBaseUnit(other.value);
        return operation.compute(baseThis, baseOther);
    }

    private double roundToTwoDecimals(double val) {
        return Math.round(val * 100.0) / 100.0;
    }

    public Quantity<U> add(Quantity<U> other) {
        return add(other, this.unit);
    }

    public Quantity<U> add(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double result = performBaseArithmetic(other, ArithmeticOperation.ADD);
        double converted = targetUnit.fromBaseUnit(result);
        return new Quantity<>(roundToTwoDecimals(converted), targetUnit);
    }

    public Quantity<U> subtract(Quantity<U> other) {
        return subtract(other, this.unit);
    }

    public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
        validateArithmeticOperands(other, targetUnit, true);
        double result = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
        double converted = targetUnit.fromBaseUnit(result);
        return new Quantity<>(roundToTwoDecimals(converted), targetUnit);
    }

    public double divide(Quantity<U> other) {
        validateArithmeticOperands(other, null, false);
        return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
    }

    public double getValue() {
        return value;
    }

    public U getUnit() {
        return unit;
    }
}