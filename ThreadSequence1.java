import java.util.concurrent.ArrayBlockingQueue;

public class ThreadSequence1 extends Thread {
    private Mechanism mechanism;
    private Hardware hardware;
    private int memory[] = new int [6];
    public static final ArrayBlockingQueue messageQueue = new ArrayBlockingQueue(10);
    private int statistics[] = new int[3];

    public ThreadSequence1(Mechanism mechanism, Hardware hardware) {
        this.mechanism = mechanism;
        this.hardware = hardware;
    }

    public static void addValueMessageQueue (Integer value) {
        messageQueue.add(value);
    }

    public int[] getStatistics() {
        return statistics;
    }

    public void run() {
        int nextDesiredPart = 0; //it needs to

        int i = 0;
        memory[0] = 0;
        memory[1] = 0;
        memory[2] = 1;
        memory[3] = 1;
        memory[4] = 2;
        memory[5] = 2;

        for (int j=0; j<3; statistics[j]=0,j++);
        while (true) {
            while ((Integer) Main.taskActive.peek() == 1) {
                ThreadIdentify.startIdentification();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer partIncoming = ThreadIdentify.peekPart();
                if (messageQueue.peek() != null) {
                    try {
                        nextDesiredPart = (Integer) messageQueue.take();
                        i--;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                //System.out.println("1 - o que vem: "+partIncoming+" o que quer: "+nextDesiredPart);
                if (partIncoming != null && partIncoming == nextDesiredPart) { //Seq1 wants the incoming part
                    if (ThreadIdentify.TryConsumeMessage() != null) {
                        //if we get here, then seq1 can take the incoming part
                        // stop conveyor
                        // do goto(1), goto(0)
                        //update next desired part
                        if (hardware.getBitValue(hardware.SafeReadPort(0), 0)) {
                            mechanism.StopConveyor();
                            mechanism.cylinder_2_Goto(1);
                            mechanism.cylinder_2_Goto(0);
                            i++;
                            if (i == memory.length) {
                                i = 0;
                                ThreadLED.addValueToMessageQueue(1000, 250);
                                ThreadLED.startLED();
                            }

                            if (nextDesiredPart == 0) {
                                statistics[0] = statistics[0] + 1;
                            } else if (nextDesiredPart == 1) {
                                statistics[1] = statistics[1] + 1;
                            } else if (nextDesiredPart == 2) {
                                statistics[2] = statistics[2] + 1;
                            }
                            try {
                                nextDesiredPart = memory[i];
                            } catch (ArrayIndexOutOfBoundsException e) {
                                i = 0;
                            }
                            mechanism.MoveConveyor();
                        }
                    }
                } else {
                    Integer partAdd = ThreadIdentify.TryConsumeMessage();
                    ThreadSequence2.addValueMessageQueue(partAdd);
                }
            }
        }
    }
}