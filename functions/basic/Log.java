package functions.basic;

import functions.Function;

public class Log implements Function {
    private final double base; // основание логарифма

    public Log(double _base) {
        if (_base <= 0 || _base == 1) {
            throw new IllegalArgumentException("Основание логарифма должно быть > 0 и != 1");
        }
        this.base = _base;
    }

    public double getLeftDomainBorder() {
        return 0; // логарифм определен только для x > 0
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    public double getFunctionValue(double x) {
        if (x <= 0) {
            return Double.NaN; // логарифм не определен для неположительных чисел
        }
        return Math.log(x) / Math.log(base); // формула перехода к новому основанию
    }
}
