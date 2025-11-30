package functions;

import java.io.*;
import java.lang.reflect.Constructor;

public class TabulatedFunctions {
    private TabulatedFunctions() {}


    // Приватное статическое поле фабрики
    private static TabulatedFunctionFactory factory = new ArrayTabulatedFunction.ArrayTabulatedFunctionFactory();

    // Метод для замены фабрики
    public static void setTabulatedFunctionFactory(TabulatedFunctionFactory newFactory){
        factory = newFactory;
    }

    // Три перегруженных метода создания табулированных функций
    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return factory.createTabulatedFunction(leftX, rightX, pointsCount);
    }

    public static TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return factory.createTabulatedFunction(leftX, rightX, values);
    }

    public static TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return factory.createTabulatedFunction(points);
    }

    // Методы с рефлексией
    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, FunctionPoint[] points) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + " не реализует интерфейс TabulatedFunction");
        }

        try {
            // Ищем конструктор с параметрами (FunctionPoint[])
            Constructor<?> constructor = functionClass.getConstructor(FunctionPoint[].class);

            return (TabulatedFunction) constructor.newInstance((Object) points);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор в классе " + functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, int pointsCount){
        // Проверяем, что класс реализует TabulatedFunciton
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)){
            throw new IllegalArgumentException("Класс " + functionClass.getName() + " не реализует интерфейс TabulatedFunction");
        }

        try {
            // Ищем конструктор с параметрами (double, double, int)
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, int.class);

            // Создаем объект с помощью рефлексии
            return (TabulatedFunction) constructor.newInstance(leftX, rightX, pointsCount);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор " + functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction createTabulatedFunction(Class<?> functionClass, double leftX, double rightX, double[] values) {
        if (!TabulatedFunction.class.isAssignableFrom(functionClass)) {
            throw new IllegalArgumentException("Класс " + functionClass.getName() + " не реализует интерфейс TabulatedFunction");
        }

        try {
            // Ищем конструктор с параметрами (double, double, double[])
            Constructor<?> constructor = functionClass.getConstructor(double.class, double.class, double[].class);

            return (TabulatedFunction) constructor.newInstance(leftX, rightX, values);

        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("Не найден конструктор в классе " + functionClass.getName(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при создании объекта " + functionClass.getName(), e);
        }
    }

    public static TabulatedFunction tabulate(Class<? extends TabulatedFunction> type, Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }

        // Используем рефлексивное создание
        return createTabulatedFunction(type, points);
    }


    public static TabulatedFunction tabulate(Function function, double leftX, double rightX, int pointsCount) {
        if (leftX < function.getLeftDomainBorder() || rightX > function.getRightDomainBorder()) {
            throw new IllegalArgumentException("Границы табулирования выходят за область определения функции");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть не менее 2");
        }

        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница должна быть меньше правой");
        }

        FunctionPoint[] points = new FunctionPoint[pointsCount];
        double step = (rightX - leftX) / (pointsCount - 1);
        for (int i = 0; i < pointsCount; i++) {
            double x = leftX + i * step;
            points[i] = new FunctionPoint(x, function.getFunctionValue(x));
        }
        return createTabulatedFunction(points);
    }

    // Метод вывода в байтовый поток
    public static void outputTabulatedFunction(TabulatedFunction function, OutputStream out) {
        try (DataOutputStream dos = new DataOutputStream(out)) {
            int pointsCount = function.getPointsCount();
            dos.writeInt(pointsCount); // Записываем количество точек

            for (int i = 0; i < pointsCount; i++) {
                dos.writeDouble(function.getPointX(i)); // Записываем x
                dos.writeDouble(function.getPointY(i)); // Записываем y
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи в поток", e);
        }
    }

    // Метод ввода из байтового потока
    public static TabulatedFunction inputTabulatedFunction(InputStream in, Class<? extends TabulatedFunction> type) {
        try (DataInputStream dis = new DataInputStream(in)) {
            int pointsCount = dis.readInt(); // Читаем количество точек

            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                double x = dis.readDouble(); // Читаем x
                double y = dis.readDouble(); // Читаем y
                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(type, points);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из потока", e);
        }
    }

    public static TabulatedFunction inputTabulatedFunction(InputStream in) {
        try (DataInputStream dis = new DataInputStream(in)) {
            int pointsCount = dis.readInt(); // Читаем количество точек

            FunctionPoint[] points = new FunctionPoint[pointsCount];
            for (int i = 0; i < pointsCount; i++) {
                double x = dis.readDouble(); // Читаем x
                double y = dis.readDouble(); // Читаем y
                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(points);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из потока", e);
        }
    }

    // Метод записи в символьный поток
    public static void writeTabulatedFunction(TabulatedFunction function, Writer out) {
        try (BufferedWriter writer = new BufferedWriter(out)) {
            // Записываем количество точек
            writer.write(String.valueOf(function.getPointsCount()));
            writer.write(" "); // пробел после числа

            // Записываем все точки в одну строку через пробелы
            for (int i = 0; i < function.getPointsCount(); i++) {
                writer.write(String.valueOf(function.getPointX(i)));
                writer.write(" ");
                writer.write(String.valueOf(function.getPointY(i)));
                writer.write(" ");
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при записи в поток", e);
        }
    }

    // Метод чтения из символьного потока
    public static TabulatedFunction readTabulatedFunction(Reader in) {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);

            // Читаем количество точек
            tokenizer.nextToken();
            int pointsCount = (int) tokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[pointsCount];

            // Читаем точки
            for (int i = 0; i < pointsCount; i++) {
                // Читаем X
                tokenizer.nextToken();
                double x = tokenizer.nval;

                // Читаем Y
                tokenizer.nextToken();
                double y = tokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(points);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из потока", e);
        }
    }

    public static TabulatedFunction readTabulatedFunction(Reader in, Class<? extends TabulatedFunction> type) {
        try {
            StreamTokenizer tokenizer = new StreamTokenizer(in);

            // Читаем количество точек
            tokenizer.nextToken();
            int pointsCount = (int) tokenizer.nval;

            FunctionPoint[] points = new FunctionPoint[pointsCount];

            // Читаем точки
            for (int i = 0; i < pointsCount; i++) {
                // Читаем X
                tokenizer.nextToken();
                double x = tokenizer.nval;

                // Читаем Y
                tokenizer.nextToken();
                double y = tokenizer.nval;

                points[i] = new FunctionPoint(x, y);
            }

            return createTabulatedFunction(type, points);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при чтении из потока", e);
        }
    }

}