package functions;

public interface Function {
    //  возвращает значение левой границы области определения функции
    public double getLeftDomainBorder();

    // возвращает значение правой границы области определения функции
    public double getRightDomainBorder();

    // возвращает значение функции в заданной точке
    public double getFunctionValue(double x);

}
