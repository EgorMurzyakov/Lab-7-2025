package functions.basic;

import functions.Function;

public abstract class TrigonometricFunction implements Function {

    public double getLeftDomainBorder() {
        return Double.NEGATIVE_INFINITY; // тригонометрические функции определены везде
    }

    public double getRightDomainBorder() {
        return Double.POSITIVE_INFINITY;
    }

    // Абстрактный метод конкретные реализации будут в наследниках
    public abstract double getFunctionValue(double x);
}