package functions;

public interface TabulatedFunction extends Function, Cloneable, Iterable<FunctionPoint> {

    // возвращаем количество точек
    int getPointsCount();

    // возвращаем копию точки
    FunctionPoint getPoint(int index);

    // заменяем указанную точку на переданную
    void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException;

    // возвращаем значение абсциссы точки с указанным номером
    double getPointX(int index);

    // изменяем значение абсциссы точки с указанным номером
    void setPointX(int index, double x) throws InappropriateFunctionPointException;

    // возвращаем значение ординаты точки с указанным номером
    double getPointY(int index);

    // изменяем значение ординаты точки с указанным номером
    void setPointY(int index, double y);

    // удаляем заданную точку
    void deletePoint(int index);

    // добавляем новую точку
    void addPoint(FunctionPoint point) throws InappropriateFunctionPointException;

    Object clone();
}
