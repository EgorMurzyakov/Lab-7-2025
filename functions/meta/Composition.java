package functions.meta;

import functions.Function;

public class Composition implements Function {
    private Function f1;
    private Function f2;

    public Composition(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public double getLeftDomainBorder() {
        return f1.getLeftDomainBorder(); // Область определения f1
    }

    public double getRightDomainBorder() {
        return f1.getRightDomainBorder(); // Область определения f1
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            double f2Value = f2.getFunctionValue(x);
            if (f2Value >= f1.getLeftDomainBorder() && f2Value <= f1.getRightDomainBorder()) {
                return f1.getFunctionValue(f2Value);
            }
        }
        return Double.NaN;
    }
}
