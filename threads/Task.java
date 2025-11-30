package threads;

import functions.Function;

public class Task {
    private Function function;
    private double leftX;
    private double rightX;
    private double step;
    private int tasksCount;
    private volatile boolean completed; // Флаг завершения всех заданий
    private volatile boolean dataReady; // Флаг готовности данных

    public Task() {
        this.tasksCount = 100;
        this.completed = false;
        this.dataReady = false;
    }

    // Геттеры и сеттеры
    public Function getFunction() { return function; }
    public void setFunction(Function function) { this.function = function; }

    public double getLeftX() { return leftX; }
    public void setLeftX(double leftX) { this.leftX = leftX; }

    public double getRightX() { return rightX; }
    public void setRightX(double rightX) { this.rightX = rightX; }

    public double getStep() { return step; }
    public void setStep(double step) { this.step = step; }

    public int getTasksCount() { return tasksCount; }
    public void setTasksCount(int tasksCount) {
        if (tasksCount < 100) {
            throw new IllegalArgumentException("Количество заданий должно быть не менее 100");
        }
        this.tasksCount = tasksCount;
    }

    public boolean isCompleted() { return completed; }
    public void setCompleted(boolean completed) { this.completed = completed; }

    public boolean isDataReady() { return dataReady; }
    public void setDataReady(boolean dataReady) { this.dataReady = dataReady; }
}
