package threads;

public class Synchronizer {
    private boolean canWrite = true;    // Изначально можно писать
    private boolean canRead = false;    // Изначально нельзя читать

    public synchronized void startWrite() throws InterruptedException {
        // Ждём, пока не разрешат писать
        while (!canWrite) {
            wait();
        }
        // Когда разрешили писать, запрещаем дальнейшую запись
        // до тех пор, пока данные не будут прочитаны
        canWrite = false;
    }

    public synchronized void endWrite() {
        // После записи разрешаем чтение
        canRead = true;
        notifyAll();
    }

    public synchronized void startRead() throws InterruptedException {
        // Ждём, пока не разрешат читать
        while (!canRead) {
            wait();
        }
        // Когда разрешили читать, запрещаем дальнейшее чтение
        // до тех пор, пока не будут записаны новые данные
        canRead = false;
    }

    public synchronized void endRead() {
        // После чтения разрешаем запись
        canWrite = true;
        notifyAll();
    }
}