package functions.meta;

import functions.Function;

public class Mult implements Function {
    private Function f1;
    private Function f2;

    public Mult(Function f1, Function f2) {
        this.f1 = f1;
        this.f2 = f2;
    }

    public double getLeftDomainBorder() {
        // Пересечение областей определения исходных функций, берем максимальную левую границу
        return Math.max(f1.getLeftDomainBorder(), f2.getLeftDomainBorder());
    }

    public double getRightDomainBorder() {
        // Пересечение областей определения исходных функций, берем минимальную правую границу
        return Math.min(f1.getRightDomainBorder(), f2.getRightDomainBorder());
    }

    public double getFunctionValue(double x) {
        // Проверяем, что x входит в пересечение областей определения
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            return f1.getFunctionValue(x) * f2.getFunctionValue(x);
        }
        return Double.NaN;
    }
}
