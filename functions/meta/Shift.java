package functions.meta;

import functions.Function;

public class Shift implements Function {
    private Function f;
    private double xShift;
    private double yShift;

    public Shift(Function f, double xShift, double yShift) {
        this.f = f;
        this.xShift = xShift;
        this.yShift = yShift;
    }

    public double getLeftDomainBorder() {
        return f.getLeftDomainBorder() + xShift;
    }

    public double getRightDomainBorder() {
        return f.getRightDomainBorder() + xShift;
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            return f.getFunctionValue(x) + yShift;
        }
        return Double.NaN;
    }
}
