package LabWork2;
public class Cylinder_2 implements Cylinder {
    Hardware hardware;

    public Cylinder_2(Hardware __hardware) {
        this.hardware = __hardware;
    }

    @Override
    public void moveForward() {
        hardware.enterCriticalArea();
        int v = hardware.SafeReadPort(2);
        v = hardware.setBitValue(v, 4, true);
        v = hardware.setBitValue(v, 3, false);
        hardware.SafeWritePort(2, v);
        hardware.leaveCriticalArea();
    }

    @Override
    public void moveBackward() {
        hardware.enterCriticalArea();
        int v = hardware.SafeReadPort(2);
        v = hardware.setBitValue(v, 3, true);
        v = hardware.setBitValue(v, 4, false);
        hardware.SafeWritePort(2, v);
        hardware.leaveCriticalArea();
    }

    @Override
    public void stop() {
        hardware.enterCriticalArea();
        int v = hardware.SafeReadPort(2);
        v = hardware.setBitValue(v, 1, false);
        v = hardware.setBitValue(v, 0, false);
        hardware.SafeWritePort(2, v);
        hardware.leaveCriticalArea();
    }

    @Override
    public void Goto(int position) {
        // go to labwork1, copy goto_x() code here and
        // proceed to the necessary adjustments
        if (getPosition() < position) {
            moveForward();
        }
        else if (getPosition() > position) {
            moveBackward();
        }
        //   while position not reached
        while (getPosition() != position){
            try {
                Thread.sleep(1);
            }catch(InterruptedException e){
                System.out.println("exception thrown: "+e);
            }
        }
        stop();
    }

    @Override
    public int getPosition() {
        int v = hardware.SafeReadPort(0);
        if (!hardware.getBitValue(v, 4)) return 0;
        else if (!hardware.getBitValue(v, 3)) return 1;
        return -1;
    }
}
