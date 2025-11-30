package threads;

import functions.Functions;
import functions.Function;

public class SimpleIntegrator implements Runnable {
    private final Task task;

    public SimpleIntegrator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        int processedCount = 0;

        while (!task.isCompleted() || task.isDataReady()) {
            // Проверяем, готовы ли данные для обработки
            if (task.isDataReady()) {
                // Читаем данные из задания
                double leftX = task.getLeftX();
                double rightX = task.getRightX();
                double step = task.getStep();
                Function function = task.getFunction();

                // Вычисляем интеграл
                try {
                    double result = Functions.integrate(function, leftX, rightX, step);
                    System.out.printf("Result %.6f %.6f %.6f %.6f%n", leftX, rightX, step, result);
                    processedCount++;
                } catch (Exception e) {
                    System.out.println("Ошибка при интегрировании: " + e.getMessage());
                }

                // Помечаем данные как обработанные
                task.setDataReady(false);
            }

            // Короткая пауза
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        System.out.println("Integrator завершил работу, обработано: " + processedCount);
    }
}