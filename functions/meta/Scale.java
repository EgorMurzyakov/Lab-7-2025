package functions.meta;

import functions.Function;

public class Scale implements Function {
    private Function f;
    private double xScale;
    private double yScale;

    public Scale(Function f, double xScale, double yScale) {
        this.f = f;
        this.xScale = xScale;
        this.yScale = yScale;
    }

    public double getLeftDomainBorder() {
        // Масштабируем область определения
        if (xScale > 0) {
            return f.getLeftDomainBorder() * xScale;
        } else if (xScale < 0) {
            return f.getRightDomainBorder() * xScale;
        } else {
            return Double.NaN; // Нулевой масштаб, функция не определена
        }
    }

    public double getRightDomainBorder() {
        // Масштабируем область определения
        if (xScale > 0) {
            return f.getRightDomainBorder() * xScale;
        } else if (xScale < 0) {
            return f.getLeftDomainBorder() * xScale;
        } else {
            return Double.NaN;
        }
    }

    public double getFunctionValue(double x) {
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            return f.getFunctionValue(x) * yScale;
        }
        return Double.NaN;
    }
}