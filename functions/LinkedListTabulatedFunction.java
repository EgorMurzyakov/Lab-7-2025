package functions;

import java.io.Serializable;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListTabulatedFunction implements TabulatedFunction, Serializable, TabulatedFunctionFactory {
    private static class FunctionNode implements Serializable {
        FunctionPoint point;
        FunctionNode prev;
        FunctionNode next;

        FunctionNode(double x, double y) {
            this.point = new FunctionPoint(x, y);
        }

        FunctionNode() {
            this.point = new FunctionPoint();
        }
    }

    private final FunctionNode head;
    private int pointsCount;
    private FunctionNode lastUsedPoint;
    private int indexLastUsedPoint;

    public LinkedListTabulatedFunction(FunctionPoint[] points) { // конструктор, получающий сразу все точки функции в виде массива объектов типа FunctionPoint
        if (points.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше или равно двум");
        }

        for (int i = 1; i < points.length; i++) { // Проверка упорядоченности абсцисс
            if (points[i].getX() <= points[i - 1].getX()) {
                throw new IllegalArgumentException("Точки не упорядочены по возрастанию абсциссы");
            }
        }

        this.head = new FunctionNode();
        head.prev = head;
        head.next = head;
        this.lastUsedPoint = head;
        this.indexLastUsedPoint = -1;

        for (FunctionPoint point : points) { // Создание копий точек для инкапсуляции
            addNodeToTail().point = new FunctionPoint(point);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, int pointsCount) { // создаёт объект табулированной функции по заданным левой и правой границе области определения, а также количеству точек для табулирования
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения должна быть строго меньше правой");
        }

        if (pointsCount < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше или равно двум");
        }

        this.head = new FunctionNode();
        head.prev = head;
        head.next = head;
        this.lastUsedPoint = head;
        this.indexLastUsedPoint = -1;

        double step = (rightX - leftX) / (pointsCount - 1); // шаг
        FunctionNode newNode;
        for (int i = 0; i < pointsCount; i++) {
            addNodeToTail().point = new FunctionPoint(leftX + step * i, 0);
        }
    }

    public LinkedListTabulatedFunction(double leftX, double rightX, double[] values) { // аналогичен предыдущему конструктору, но вместо количества точек получает значения функции в виде массива
        if (leftX >= rightX) {
            throw new IllegalArgumentException("Левая граница области определения должна быть строго меньше правой");
        }

        if (values.length < 2) {
            throw new IllegalArgumentException("Количество точек должно быть больше или равно двум");
        }

        this.head = new FunctionNode();
        head.prev = head;
        head.next = head;
        this.lastUsedPoint = head;
        this.indexLastUsedPoint = -1;

        double step = (rightX - leftX) / (pointsCount - 1); // шаг
        FunctionNode newNode;
        for (int i = 0; i < values.length; i++) {
            addNodeToTail().point = new FunctionPoint(leftX + step * i, values[i]);
        }
    }

    private LinkedListTabulatedFunction() { // Конструктор создающий пустой список
        this.head = new FunctionNode();
        head.prev = head;
        head.next = head;
        this.pointsCount = 0;
        lastUsedPoint = head;
        indexLastUsedPoint = -1;
    }

    private FunctionNode getNodeByIndex(int index) {
        if (index >= 0 && index < pointsCount) {
            int currentIndex;
            FunctionNode currentNode;

            if (Math.abs(indexLastUsedPoint - index) >= index) { // index ближе к head
                currentNode = head.next; // Первый элемент (индекс = 0)
                currentIndex = 0;
                while (currentIndex != index) {
                    currentNode = currentNode.next;
                    currentIndex++;
                }
            } else { // index ближе к последней используемой точки
                currentNode = lastUsedPoint;
                currentIndex = indexLastUsedPoint;

                if (indexLastUsedPoint >= index) { // Проверяем в какую сторону идти от текущего элемента
                    while (currentIndex != index) {
                        currentNode = currentNode.prev;
                        currentIndex--;
                    }
                } else {
                    while (currentIndex != index) {
                        currentNode = currentNode.next;
                        currentIndex++;
                    }
                }
            }
            lastUsedPoint = currentNode;
            indexLastUsedPoint = currentIndex;
            return currentNode;
        } else {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы");
        }
    }

    private FunctionNode addNodeToTail() { // Добавление в конец
        FunctionNode newNode = new FunctionNode();
        head.prev.next = newNode;
        newNode.prev = head.prev;
        head.prev = newNode;
        newNode.next = head;

        pointsCount++;
        return newNode;
    }

    private FunctionNode addNodeByIndex(int index) { // Добавление по индексу
        if (index >= 0 && index < pointsCount) {
            int currentIndex;
            FunctionNode currentNode;
            FunctionNode newNode = new FunctionNode();

            if (Math.abs(indexLastUsedPoint - index) >= index) { // index ближе к head
                currentNode = head.next; // Первый элемент (индекс = 0)
                currentIndex = 0;
                while (currentIndex != index) {
                    currentNode = currentNode.next;
                    currentIndex++;
                }
            } else { // index ближе к последней используемой точки
                currentNode = lastUsedPoint;
                currentIndex = indexLastUsedPoint;

                if (indexLastUsedPoint >= index) { // Проверяем в какую сторону идти от текущего элемента
                    while (currentIndex != index) {
                        currentNode = currentNode.prev;
                        currentIndex--;
                    }
                } else {
                    while (currentIndex != index) {
                        currentNode = currentNode.next;
                        currentIndex++;
                    }
                }
            }
            currentNode.prev.next = newNode;
            newNode.prev = currentNode.prev;
            currentNode.prev = newNode;
            newNode.next = currentNode;

            lastUsedPoint = currentNode;
            indexLastUsedPoint = currentIndex + 1;
            pointsCount++;
            return newNode;
        } else {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы");
        }
    }

    private FunctionNode deleteNodeByIndex(int index) { // Удаление по индексу
        if (pointsCount < 3) {
            throw new IllegalStateException("Точек в наборе не должно остаться менее двух");
        } else if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы");
        } else {
            int currentIndex;
            FunctionNode currentNode;

            if (Math.abs(indexLastUsedPoint - index) >= index) { // index ближе к head
                currentNode = head.next; // Первый элемент (индекс = 0)
                currentIndex = 0;
                while (currentIndex != index) {
                    currentNode = currentNode.next;
                    currentIndex++;
                }
            } else { // index ближе к последней используемой точки
                currentNode = lastUsedPoint;
                currentIndex = indexLastUsedPoint;

                if (indexLastUsedPoint >= index) { // Проверяем в какую сторону идти от текущего элемента
                    while (currentIndex != index) {
                        currentNode = currentNode.prev;
                        currentIndex--;
                    }
                } else {
                    while (currentIndex != index) {
                        currentNode = currentNode.next;
                        currentIndex++;
                    }
                }
            }
            currentNode.prev.next = currentNode.next;
            currentNode.next.prev = currentNode.prev;
            currentNode.prev = null;
            currentNode.next = null;

            lastUsedPoint = currentNode.prev;
            indexLastUsedPoint = currentIndex - 1;
            pointsCount--;
            return currentNode;
        }
    }

    public double getLeftDomainBorder() {
        return head.next.point.getX(); // возвращаем абсциссу самой левой (первой) точки
    }

    public double getRightDomainBorder() {
        return head.prev.point.getX(); // возвращаем абсциссу самой правой (последней) точки
    }

    public double getFunctionValue(double x) { // возвращаем значение функции в точке x
        if (x >= getLeftDomainBorder() && x <= getRightDomainBorder()) {
            FunctionNode currentNode;


            if (lastUsedPoint == head || Math.abs(lastUsedPoint.point.getX() - x) >= x) {
                currentNode = head.next;

                while (!(currentNode.point.getX() >= x)) { // ищем индекс точки с абсциссой большей или равной входному значению x
                    currentNode = currentNode.next;
                }
            } else {
                if (lastUsedPoint.point.getX() < x) {
                    while (!(lastUsedPoint.point.getX() >= x)) { // ищем индекс точки с абсциссой большей или равной входному значению x
                        lastUsedPoint = lastUsedPoint.next;
                        indexLastUsedPoint++;
                    }
                } else {
                    while (!(lastUsedPoint.point.getX() < x)) {
                        lastUsedPoint = lastUsedPoint.prev;
                        indexLastUsedPoint--;
                    }
                    lastUsedPoint = lastUsedPoint.next;
                    indexLastUsedPoint++;
                }
                currentNode = lastUsedPoint;
            }

            if (Math.abs(currentNode.prev.point.getX() - x) < 1e-10) { // проверяем предыдущую точку на равенство ее абсциссы с входным значением x
                return currentNode.prev.point.getY();
            } else if (Math.abs(currentNode.point.getX() - x) < 1e-10) { // проверяем текущую точку на равенство ее абсциссы с входным значением x
                return currentNode.point.getY();
            } else { // иначе используем линейную интерполяцию
                double x1 = currentNode.prev.point.getX();
                double x2 = currentNode.point.getX();
                double y1 = currentNode.prev.point.getY();
                double y2 = currentNode.point.getY();
                return (x - x1) * (y2 - y1) / (x2 - x1) + y1;
            }
        } else {
            return Double.NaN;
        }
    }

    public int getPointsCount() { // возвращаем количество точек
        return pointsCount;
    }

    public FunctionPoint getPoint(int index) { // возвращаем копию точки
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
        }

        return new FunctionPoint(getNodeByIndex(index).point);
    }

    public void setPoint(int index, FunctionPoint point) throws InappropriateFunctionPointException { // заменяем указанную точку на переданную
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
        }

        if (index == 0) { // Проверяем граничные случаи
            if (point.getX() < head.next.next.point.getX()) {
                head.next.point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("Координата x заданной точки должна быть меньше следующей");
            }
        } else if (index == pointsCount - 1) {
            if (head.prev.prev.point.getX() < point.getX()) {
                head.prev.point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("Координата x заданной точки должна быть больше следующей");
            }
        } else {
            if ((point.getX() < getNodeByIndex(index + 1).point.getX()) && (getNodeByIndex(index - 1).point.getX() < point.getX())) {
                getNodeByIndex(index).point = new FunctionPoint(point);
            } else {
                throw new InappropriateFunctionPointException("Координата x заданной точки должна находиться в интервале между соседними точками");
            }
        }
    }

    public double getPointX(int index) { // возвращаем значение абсциссы точки с указанным номером
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
        }

        return getNodeByIndex(index).point.getX();
    }

    public void setPointX(int index, double x) throws InappropriateFunctionPointException { // изменяем значение абсциссы точки с указанным номером
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
        }

        if (index == 0) { // Проверяем граничные случаи
            if (x < head.next.next.point.getX()) {
                head.next.point.setX(x);
            } else {
                throw new InappropriateFunctionPointException("Координата x заданной точки должна быть меньше следующей");
            }
        } else if (index == pointsCount - 1) {
            if (head.prev.prev.point.getX() < x) {
                head.prev.point.setX(x);
            } else {
                throw new InappropriateFunctionPointException("Координата x заданной точки должна быть больше следующей");
            }
        } else {
            if ((x < getNodeByIndex(index + 1).point.getX()) && (getNodeByIndex(index - 1).point.getX() < x)) {
                getNodeByIndex(index).point.setX(x);
            } else {
                throw new InappropriateFunctionPointException("Координата x заданной точки должна находиться в интервале между соседними точками");
            }
        }
    }

    public double getPointY(int index) { // возвращаем значение ординаты точки с указанным номером
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
        }

        return getNodeByIndex(index).point.getY();
    }

    public void setPointY(int index, double y) { // изменяем значение ординаты точки с указанным номером
        if (index < 0 || index >= pointsCount) {
            throw new FunctionPointIndexOutOfBoundsException("Переданный номер выходит за границы набора точек");
        }

        getNodeByIndex(index).point.setY(y);
    }

    public void deletePoint(int index) { // удаляем заданную точку
        deleteNodeByIndex(index);
    }

    public void addPoint(FunctionPoint point) throws InappropriateFunctionPointException { // добавляем новую точку
        for (int i = 0; i < pointsCount; i++) {
            if (Math.abs(getNodeByIndex(i).point.getX() - point.getX()) < 1e-10) {
                throw new InappropriateFunctionPointException("Координата x новой точки совпадает с абсциссой уже существующей точки");
            }
        }

        if (point.getX() < head.next.point.getX()) {
            addNodeByIndex(0).point = new FunctionPoint(point);
        } else if (point.getX() > head.prev.point.getX()) {
            addNodeToTail().point = new FunctionPoint(point);
        } else {
            int i = 0;
            while (!((getNodeByIndex(i).point.getX() < point.getX()) && (getNodeByIndex(i + 1).point.getX() > point.getX()))) {
                i++;
            }
            addNodeByIndex(i + 1).point = new FunctionPoint(point);
        }
    }

    @Override
    public String toString() {
        String str = "";
        FunctionNode current = head.next;
        str += current.point;
        current = current.next;
        while (current != head) {
            str += ", " + current.point;
            current = current.next;
        }
        return '{' + str + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TabulatedFunction)) return false;

        if (o instanceof LinkedListTabulatedFunction) {
            // Оптимизация для LinkedListTabulatedFunction
            LinkedListTabulatedFunction that = (LinkedListTabulatedFunction) o;
            if (this.pointsCount != that.pointsCount) return false;

            FunctionNode thisCurrent = this.head.next;
            FunctionNode thatCurrent = that.head.next;

            while (thisCurrent != this.head) {
                if (!thisCurrent.point.equals(thatCurrent.point)) return false;
                thisCurrent = thisCurrent.next;
                thatCurrent = thatCurrent.next;
            }
            return true;
        } else {
            // Универсальный подход для любого TabulatedFunction
            TabulatedFunction that = (TabulatedFunction) o;
            if (this.pointsCount != that.getPointsCount()) return false;

            FunctionNode current = this.head.next;
            int index = 0;
            while (current != this.head) {
                FunctionPoint thatPoint = that.getPoint(index);
                if (!current.point.equals(thatPoint)) return false;
                current = current.next;
                index++;
            }
            return true;
        }
    }

    @Override
    public int hashCode() {
        int hash = pointsCount; // Начинаем с количества точек

        // XOR с хэш-кодами всех точек
        FunctionNode current = head.next;
        while (current != head) {
            hash ^= current.point.hashCode();
            current = current.next;
        }

        return hash;
    }

    @Override
    public Object clone() {
        FunctionPoint[] clonePoints = new FunctionPoint[pointsCount];
        FunctionNode current = head.next;
        for (int i = 0; i < pointsCount; i++) {
            clonePoints[i] = (FunctionPoint) current.point.clone();
            current = current.next;
        }
        return new LinkedListTabulatedFunction(clonePoints);
    }

    // ----- Лабораторная работа номер 7 -----

    @Override
    public Iterator<FunctionPoint> iterator() {
        return new Iterator<FunctionPoint>() {
            private FunctionNode currentNode = head.next;

            @Override
            public boolean hasNext() {
                return currentNode != head;
            }

            @Override
            public FunctionPoint next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("Следующего элемента не существует");
                }
                FunctionPoint point = new FunctionPoint(currentNode.point);
                currentNode = currentNode.next;
                return point;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("Удаление недоступно");
            }
        };
    }

    @Override
    public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
        return new LinkedListTabulatedFunction(points);
    }

    @Override
    public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
        return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
    }

    @Override
    public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
        return new LinkedListTabulatedFunction(leftX, rightX, values);
    }

    // Класс фабрики
    public static class LinkedListTabulatedFunctionFactory implements TabulatedFunctionFactory {
        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, int pointsCount) {
            return new LinkedListTabulatedFunction(leftX, rightX, pointsCount);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(double leftX, double rightX, double[] values) {
            return new LinkedListTabulatedFunction(leftX, rightX, values);
        }

        @Override
        public TabulatedFunction createTabulatedFunction(FunctionPoint[] points) {
            return new LinkedListTabulatedFunction(points);
        }
    }
}
