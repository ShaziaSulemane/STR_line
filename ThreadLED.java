import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Semaphore;

public class ThreadLED extends Thread{
    //led p2.7
    //pisca durante 1 segundo para bancada 1
    //pisca durante 2 segundos para a bancada 2

    private Hardware hardware;
    private static final Semaphore semaphore = new Semaphore(0);
    public static final ArrayBlockingQueue messageQueue = new ArrayBlockingQueue(10);
    static Integer lock = new Integer(0);

    public ThreadLED(Hardware hardware) {
        this.hardware = hardware;
    }

    public static void startLED(){
        semaphore.release();
    }

    public static void addValueToMessageQueue (Integer n_millis, Integer interval_millis) {
        messageQueue.add(n_millis);
        messageQueue.add(interval_millis);
    }

    public void run () {
        Integer n_millis;
        Integer interval_millis;

        while (true){
            synchronized (lock) {
                try {
                    ThreadLED.semaphore.acquire();
                    n_millis = (Integer) messageQueue.take();
                    interval_millis = (Integer) messageQueue.take();
                    //System.out.println("interval_millis: " + interval_millis + " n_millis: " + n_millis);
                    long t = System.currentTimeMillis();
                    while (System.currentTimeMillis() - t < n_millis) {
                        hardware.enterCriticalArea();
                        int byte2 = hardware.SafeReadPort(2);
                        byte2 = hardware.setBitValue(byte2, 7, true);
                        hardware.SafeWritePort(2,byte2);
                        hardware.leaveCriticalArea();
                        try {
                            Thread.sleep(interval_millis);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        hardware.enterCriticalArea();
                        int byte22 = hardware.setBitValue(hardware.SafeReadPort(2), 7, false);
                        hardware.SafeWritePort(2,byte22);
                        hardware.leaveCriticalArea();
                        try {
                            Thread.sleep(interval_millis);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ThreadLED.semaphore.drainPermits();
            }
        }
    }
}
