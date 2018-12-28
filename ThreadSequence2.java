import java.util.concurrent.ArrayBlockingQueue;

public class ThreadSequence2 extends Thread{
    private Mechanism mechanism;
    private Hardware hardware;
    private int memory[] = new int [6];
    public static final ArrayBlockingQueue messageQueue = new ArrayBlockingQueue(10);
    public static final ArrayBlockingQueue requestQueue = new ArrayBlockingQueue(10);
    private int statistics[] = new int[3];
    private int Rstatistics[] = new int[3];

    public ThreadSequence2(Mechanism mechanism, Hardware hardware) {
        this.mechanism = mechanism;
        this.hardware = hardware;
    }

    public static void addValueMessageQueue(Integer value){
        if (value != null) {
            messageQueue.add(value);
        }
    }

    public static void requestMessageQueue (Integer request) {
        requestQueue.add(request);
    }

    public int[] getStatistics() {
        return statistics;
    }

    public int[] getRstatistics() {
        return Rstatistics;
    }

    public void run() {
        int nextDesiredPart = 0; //it needs to

        int i = 0;
        memory[0] = 0;
        memory[1] = 1;
        memory[2] = 2;
        memory[3] = 0;
        memory[4] = 1;
        memory[5] = 2;
        while (true) {
            while ((Integer) Main.taskActive.peek() == 1) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    Integer partIncoming = (Integer) messageQueue.take();
                    if (requestQueue.peek() != null) {
                        try {
                            nextDesiredPart = (Integer) requestQueue.take();
                            i--;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    //System.out.println("2 - o que vem: " + partIncoming + " o que quer: " + nextDesiredPart);
                    if (partIncoming != null && partIncoming == nextDesiredPart) { //Seq1 wants the incoming part
                        //if we get here, then seq1 can take the incoming part
                        // stop conveyor
                        // do goto(1), goto(0)
                        //update next desired part
                        if (!hardware.getBitValue(hardware.SafeReadPort(1), 7)) {
                            while (!hardware.getBitValue(hardware.SafeReadPort(1), 7)) {
                                try {
                                    Thread.sleep(1);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            mechanism.StopConveyor();
                            mechanism.cylinder_3_Goto(1);
                            mechanism.cylinder_3_Goto(0);
                            i++;
                            if (i == memory.length) {
                                i = 0;
                                ThreadLED.addValueToMessageQueue(2000, 500);
                                ThreadLED.startLED();
                            }
                            if (nextDesiredPart == 0) {
                                statistics[0] = statistics[0] + 1;
                            } else if (nextDesiredPart == 1) {
                                statistics[1] = statistics[1] + 1;
                            } else if (nextDesiredPart == 2) {
                                statistics[2] = statistics[2] + 1;
                            }
                            nextDesiredPart = memory[i];
                            mechanism.MoveConveyor();
                        }
                    } else {
                        if (partIncoming == 0) {
                            Rstatistics[0] = Rstatistics[0] + 1;
                        } else if (partIncoming == 1) {
                            Rstatistics[1] = Rstatistics[1] + 1;
                        } else if (partIncoming == 2) {
                            Rstatistics[2] = Rstatistics[2] + 1;
                        }
                        messageQueue.clear();
                        mechanism.MoveConveyor();
                        ThreadLED.addValueToMessageQueue(1000, 100);
                        ThreadLED.startLED();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
