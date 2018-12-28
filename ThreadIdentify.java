import java.util.concurrent.*;

public class ThreadIdentify extends Thread{

    private Mechanism mechanism;
    private static final Semaphore semaphore = new Semaphore(0);
    public static final ArrayBlockingQueue messageQueue = new ArrayBlockingQueue(10);
    static Integer lock = new Integer(0);

    public ThreadIdentify(Mechanism mechanism) {
        this.mechanism = mechanism;
    }

    public static void startIdentification() {
        semaphore.release();
    }

    public static void drainPermits () {
        semaphore.drainPermits();
    }

    public static Integer peekPart() {
        return (Integer)messageQueue.peek();
    }

    public static Integer consumeMessage() {
        try {
            return (Integer)messageQueue.take();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Integer TryConsumeMessage() {
        synchronized (lock) {
            if (messageQueue.peek() != null) {
                try {
                    return (Integer) messageQueue.take();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }
    }

    public void run() {
        System.out.println("ThreadIdentify just started....");
        while(true) {
            try {
                ThreadIdentify.semaphore.acquire();
                int part = mechanism.identify();
                messageQueue.add(part);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}


