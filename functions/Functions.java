package functions;

import functions.meta.*;

public class Functions {
    private Functions() {}

    public static Function shift(Function f, double shiftX, double shiftY) {
        return new Shift(f, shiftX, shiftY);
    }

    public static Function scale(Function f, double scaleX, double scaleY) {
        return new Scale(f, scaleX, scaleY);
    }

    public static Function power(Function f, double power) {
        return new Power(f, power);
    }

    public static Function sum(Function f1, Function f2) {
        return new Sum(f1, f2);
    }

    public static Function mult(Function f1, Function f2) {
        return new Mult(f1, f2);
    }

    public static Function composition(Function f1, Function f2) {
        return new Composition(f1, f2);
    }

    // ------------ LR - 6 ------------

    public static double integrate(Function function, double leftX, double rightX, double step) {
        // Проверяем границы области определения
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Интервал интегрирования выходит за границы области определения функции");
        }

        if (step <= 0) {
            throw new IllegalArgumentException("Шаг должен быть положительным");
        }

        double integral = 0.0;
        double currentX = leftX;

        // Метод трапеций
        while (currentX < rightX) {
            double nextX = Math.min(currentX + step, rightX);
            double fCurrent = function.getFunctionValue(currentX);
            double fNext = function.getFunctionValue(nextX);

            // Площадь трапеции: (f(a) + f(b)) * (b - a) / 2
            integral += (fCurrent + fNext) * (nextX - currentX) / 2;
            currentX = nextX;
        }

        return integral;
    }

}
