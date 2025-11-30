package functions;

public interface TabulatedFunctionFactory {
    TabulatedFunction createTabulatedFunction(FunctionPoint[] points);
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount);
    TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values);
}