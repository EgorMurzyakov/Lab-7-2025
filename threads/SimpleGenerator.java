package threads;

import functions.Function;
import functions.basic.Log;

public class SimpleGenerator implements Runnable {
    private final Task task;

    public SimpleGenerator(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        for (int i = 0; i < task.getTasksCount(); i++) {
            // Генерируем новые данные
            double base = 1.001 + Math.random() * 8.999;
            Function logFunction = new Log(base);
            double leftX = 0.001 + Math.random() * 99.999;
            double rightX = 100 + Math.random() * 100;
            double step = Math.random();

            // Устанавливаем данные в задание
            task.setFunction(logFunction);
            task.setLeftX(leftX);
            task.setRightX(rightX);
            task.setStep(step);

            // Помечаем данные как готовые
            task.setDataReady(true);

            System.out.printf("Source %.6f %.6f %.6f%n", leftX, rightX, step);

            // Ждем пока интегратор обработает данные
            while (task.isDataReady()) {
                try {
                    Thread.sleep(1); // Короткая пауза вместо синхронизации
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }

        // Помечаем завершение работы
        task.setCompleted(true);
        System.out.println("Generator завершил работу");
    }
}