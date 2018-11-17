package LabWork2;

public class Mechanism {
    Hardware hardware;
    private Cylinder cyl_1, cyl_2, cyl_3;
    public Mechanism(Hardware hard, Cylinder c1, Cylinder c2, Cylinder c3) {
        this.hardware = hard;
        this.cyl_1 = c1;
        this.cyl_2 = c2;
        this.cyl_3 = c3;
    } //  ……..   REMAINING FUNCTIONS HERE …. (sensors, actuators, …)

   // Hardware hard;
   // public Mechanism(Hardware h){
    //    hard = h;
  //  }

    public void MoveConveyor(){
        hardware.enterCriticalArea();
        int x = hardware.SafeReadPort(2);
        x= hardware.setBitValue(x, 2 , true);
        hardware.SafeWritePort(2,x);
        hardware.leaveCriticalArea();
    }

    public void cylinder_1_moveBackward() {
        this. cyl_1. moveBackward();
    }

    public void cylinder_1_moveForward() {
        this. cyl_1. moveForward();
    }

    public void cylinder_1_stop() {
        this. cyl_1.stop();
    }

    public void cylinder_1_Goto(int pos) {
        this.cyl_1.Goto(pos);
    }

    public int cylinder_1_getPosition() {
        return this.cyl_1.getPosition();
    }

    public boolean cylinder_1_isAtPosition(int pos) {
        return (pos==this.cyl_1.getPosition());
    }

    public void cylinder_2_moveBackward() {
        this. cyl_2. moveBackward();
    }

    public void cylinder_2_moveForward() {
        this. cyl_2.moveForward();
    }

    public void cylinder_2_stop() {
        this. cyl_2.stop();
    }

    public void cylinder_2_Goto(int pos) { //pos = 0 or 1
        this.cyl_2.Goto(pos);
    }

    public int cylinder_2_getPosition() {
        return this.cyl_2.getPosition();
    }

    public boolean cylinder_2_isAtPosition(int pos) {
        return (pos==this.cyl_2.getPosition());
    }

    public void cylinder_3_moveBackward() {
        this. cyl_3. moveBackward();
    }

    public void cylinder_3_moveForward() {
        this. cyl_3.moveForward();
    }

    public void cylinder_3_stop() {
        this. cyl_3.stop();
    }

    public void cylinder_3_Goto(int pos) { //pos = 0 or 1
        this.cyl_3.Goto(pos);
    }

    public int cylinder_3_getPosition() {
        return this.cyl_3.getPosition();
    }

    public boolean cylinder_3_isAtPosition(int pos) {
        return (pos==this.cyl_3.getPosition());
    }

    public int identify() {
        boolean Sensor1 = false;
        boolean Sensor2 = false;
        // account the number of patches, in bits P1.5 and P1.6
        // until bit P0.0 goes to 1
        // return 0,1, or 2
        // like this
        int previous = hardware.SafeReadPort(1);
        while(!hardware.getBitValue(hardware.SafeReadPort(0),0)) {
            //account patches here
            if(hardware.getBitValue(hardware.SafeReadPort(1),5) && !hardware.getBitValue(previous,5)) Sensor1=true;
            if(hardware.getBitValue(hardware.SafeReadPort(1),6) && !hardware.getBitValue(previous,6)) Sensor2=true;
            previous = hardware.SafeReadPort(1);
        }
        if(Sensor2 && !Sensor1) return 1;
        else if(Sensor2 && Sensor1) return 2;
        else if(!Sensor1 && !Sensor2) return 0;

        return -1;
    }
}
