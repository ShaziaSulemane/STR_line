import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static final ArrayBlockingQueue taskActive = new ArrayBlockingQueue(2);

    public static void main(String []args) {
        Hardware h = new Hardware();
        h.create_di(0);
        h.create_di(1);
        h.create_do(2);

        Cylinder cyl1 = new Cylinder_1(h);
        Cylinder cyl2 = new Cylinder_2(h);
        Cylinder cyl3 = new Cylinder_3(h);

        Mechanism mechanism = new Mechanism(h, cyl1, cyl2, cyl3);

        //Thread running cylinder 1
        MoveCylinder_1 moveCyl_1 = new MoveCylinder_1(mechanism);

        //Threads running the station switches
        detectSwitch1 switch1 = new detectSwitch1(h);
        detectSwitch2 switch2 = new detectSwitch2(h);

        //Threads running the calibration process
        CalibrateCylinder2 cal_2 = new CalibrateCylinder2(mechanism);
        CalibrateCylinder3 cal_3 = new CalibrateCylinder3(mechanism);

        //Threads running the box sequence recognition
        ThreadSequence1 sequence1 = new ThreadSequence1(mechanism, h);
        ThreadSequence2 sequence2 = new ThreadSequence2(mechanism, h);

        //Thread running the parts identification
        ThreadIdentify partID = new ThreadIdentify(mechanism);

        //Thread running the main LED
        ThreadLED led1 = new ThreadLED(h);

        taskActive.add(1);

        //Menu
        int p0 = -1, p1 = -1, p2 = -1;
        BufferedReader B =  new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("MENU\n\nM|START\nS|STOP\nR|RESUME\nE|STATISTICS\nF|FINISH\n\n");
            try {
                String input = B.readLine();
                switch (input.charAt(0)) {
                    case 'm': //start
                    case 'M':
                        moveCyl_1.start();
                        mechanism.MoveConveyor();
                        cal_2.start();
                        cal_3.start();
                        switch1.start();
                        switch2.start();
                        sequence1.start();
                        sequence2.start();
                        partID.start();
                        led1.start();
                        break;
                    case 'f': //terminate
                    case 'F':
                        taskActive.clear();
                        taskActive.add(0);
                        System.exit(1);
                    case 'e': //statistics
                    case 'E':
                        int statistics1[] = sequence1.getStatistics();
                        int statistics2[] = sequence2.getStatistics();
                        int statistics3[] = sequence2.getRstatistics();
                        System.out.println("Station 1:");
                        System.out.println("Number of A boxes: " + statistics1[0]);
                        System.out.println("Number of B boxes: " + statistics1[1]);
                        System.out.println("Number of C boxes: " + statistics1[2]);
                        System.out.println("Station 2:");
                        System.out.println("Number of A boxes: " + statistics2[0]);
                        System.out.println("Number of B boxes: " + statistics2[1]);
                        System.out.println("Number of C boxes: " + statistics2[2]);
                        System.out.println("Station 3: (Recycle)");
                        System.out.println("Number of A boxes: " + statistics3[0]);
                        System.out.println("Number of B boxes: " + statistics3[1]);
                        System.out.println("Number of C boxes: " + statistics3[2]);
                        break;
                    case 's':
                    case 'S':
                        taskActive.clear();
                        taskActive.add(0);
                        p0 = h.SafeReadPort(0);
                        p1 = h.SafeReadPort(1);
                        p2 = h.SafeReadPort(2);
                        mechanism.StopConveyor();
                        mechanism.cylinder_1_stop();
                        mechanism.cylinder_2_stop();
                        mechanism.cylinder_3_stop();
                        break;
                    case 'r':
                    case 'R':
                        taskActive.add(1);
                        taskActive.take();
                        if (p0 != -1 && p1 != -1 && p2 != -1) {
                            h.enterCriticalArea();
                            h.SafeWritePort(0, p0);
                            h.SafeWritePort(1, p1);
                            h.SafeWritePort(2, p2);
                            h.leaveCriticalArea();
                        }
                        break;

                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}


