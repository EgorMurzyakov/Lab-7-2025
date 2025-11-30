package threads;

import static functions.Functions.integrate;

public class Integrator extends Thread {
    private final Task task;
    private final Synchronizer synchronizer;

    public Integrator(Task task, Synchronizer synchronizer) {
        this.task = task;
        this.synchronizer = synchronizer;
    }

    @Override
    public void run() {
        int processedCount = 0;
        try {
            for (int i = 0; i < task.getTasksCount(); i++) {
                // Проверяем прерывание
                if (isInterrupted()) {
                    System.out.println("Integrator: получен сигнал прерывания");
                    break;
                }

                // Начинаем операцию чтения
                synchronizer.startRead();

                if (isInterrupted()) {
                    synchronizer.endRead();
                    break;
                }

                // Вычисляем интеграл
                double result = integrate(task.getFunction(), task.getLeftX(), task.getRightX(), task.getStep());

                System.out.printf("Integrator: Result %.6f %.6f %.6f %.6f%n", task.getLeftX(), task.getRightX(), task.getStep(), result);
                processedCount++;
                // Завершаем чтение
                synchronizer.endRead();
            }

            System.out.println("Integrator: завершил работу, обработано: " + processedCount);

        } catch (InterruptedException e) {
            System.out.println("Integrator: был прерван во время ожидания, обработано: " + processedCount);
            Thread.currentThread().interrupt();
        }
    }
}