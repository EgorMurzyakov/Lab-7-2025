package functions.basic;

import functions.Function;

public class Exp implements Function {
    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY; // экспонента определена везде
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY; // экспонента определена везде
    }

    public double getFunctionValue(double x) {
        return Math.exp(x); // вычисляем e^x
    }
}
