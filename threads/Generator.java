package threads;

import functions.Function;
import functions.basic.Log;

public class Generator extends Thread {
    private final Task task;
    private final Synchronizer synchronizer;

    public Generator(Task task, Synchronizer synchronizer) {
        this.task = task;
        this.synchronizer = synchronizer;
    }

    @Override
    public void run() {
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверяем прерывание
                if (isInterrupted()) {
                    System.out.println("Generator: получен сигнал прерывания");
                    break;
                }

                // Начинаем операцию записи
                synchronizer.startWrite();

                if (isInterrupted()) {
                    synchronizer.endWrite();
                    break;
                }

                // Генерируем данные
                double base = 1.001 + Math.random() * 8.999;
                Function logFunction = new Log(base);
                task.setFunction(logFunction);
                task.setLeftX(0.001 + Math.random() * 99.999);
                task.setRightX(100 + Math.random() * 100);
                task.setStep(Math.random());

                System.out.printf("Generator: Source %.6f %.6f %.6f%n", task.getLeftX(), task.getRightX(), task.getStep());

                // Завершаем запись
                synchronizer.endWrite();
            }

            System.out.println("Generator: завершил работу");

        } catch (InterruptedException e) {
            System.out.println("Generator: был прерван во время ожидания");
            Thread.currentThread().interrupt();
        }
    }
}