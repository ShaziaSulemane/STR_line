public class detectSwitch2 extends Thread{

    private Hardware hardware;


    public detectSwitch2(Hardware hardware) {
        this.hardware = hardware;
    }

    public void run(){
        int p1_curr = hardware.SafeReadPort(1);
        //0001 0000 -> active station 1 -> 0x10
        //0000 1000 -> active station 2 -> 0x4
        //0001 1000 -> 0x18
        int prev_p1 = p1_curr;
        int station2 = 0;
        int p1_dummy;

        boolean _not_pressed = true;
        while (true) {
            while ((Integer) Main.taskActive.peek() == 1) {
                while (_not_pressed) {
                    p1_dummy = p1_curr & 0x18;
                    if (p1_dummy == 0x8) {
                        _not_pressed = false;
                        System.out.println("Station 2 pressed");
                    }
                    p1_curr = hardware.SafeReadPort(1);
                }
                _not_pressed = true;

                long firstTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - firstTime < 5000) {
                    p1_curr = hardware.SafeReadPort(1); //read current state
                    if (hardware.getBitValue(p1_curr, 3) && !hardware.getBitValue(prev_p1, 3)) {
                        station2++;
                        System.out.println("Station 2 pressed: " + station2);
                    }
                    prev_p1 = p1_curr;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (station2 == 1) {
                    ThreadSequence2.requestMessageQueue(0);
                    System.out.println("2) Request A");
                } else if (station2 == 2) {
                    ThreadSequence2.requestMessageQueue(1);
                    System.out.println("2) Request B");
                } else if (station2 == 3) {
                    ThreadSequence2.requestMessageQueue(2);
                    System.out.println("2) Request C");
                } else {
                    System.out.println("PACOTE INVALIDO");
                }
                station2 = 0;
                //continue ThreadSequence2
            }
        }
    }
}
