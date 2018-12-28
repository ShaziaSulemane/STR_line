public class detectSwitch1 extends Thread{

    private Hardware hardware;
    //private ThreadLED led;


    public detectSwitch1(Hardware hardware/*, ThreadLED led*/) {
        this.hardware = hardware;
       // this.led = led;
    }

    public void run(){
        int p1_curr = hardware.SafeReadPort(1);
        //0001 0000 -> active station 1 -> 0x10
        //0000 1000 -> active station 2 -> 0x4
        //0001 1000 -> 0x18
        int prev_p1 = p1_curr;
        int station1 = 0;
        int p1_dummy;

        boolean _not_pressed = true;
        while (true) {
            while ((Integer) Main.taskActive.peek() == 1) {
                while (_not_pressed) {
                    p1_dummy = p1_curr & 0x18;
                    if (p1_dummy == 0x10) {
                        _not_pressed = false;
                        System.out.println("Station 1 pressed");
                    }
                    p1_curr = hardware.SafeReadPort(1);
                }
                _not_pressed = true;

                long firstTime = System.currentTimeMillis();

                while (System.currentTimeMillis() - firstTime < 5000) {
                    p1_curr = hardware.SafeReadPort(1); //read current state
                    if (hardware.getBitValue(p1_curr, 4) && !hardware.getBitValue(prev_p1, 4)) {
                        station1++;
                        System.out.println("Station 1 pressed: " + station1);
                    }
                    prev_p1 = p1_curr;
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (station1 == 1) {
                    ThreadSequence1.addValueMessageQueue(0);
                    System.out.println("1) Request A");
                } else if (station1 == 2) {
                    ThreadSequence1.addValueMessageQueue(1);
                    System.out.println("1) Request B");
                } else if (station1 == 3) {
                    ThreadSequence1.addValueMessageQueue(2);
                    System.out.println("1) Request C");
                } else {
                    System.out.println("PACOTE INVALIDO");
                }
                station1 = 0;
                //continue ThreadSequence1
            }
        }
    }
}
