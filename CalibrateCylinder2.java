public class CalibrateCylinder2 extends Thread{
    Mechanism mechanism;

    public CalibrateCylinder2(Mechanism mechanism){
        this.mechanism = mechanism;
    }

    public void run () {
        while (!mechanism.cylinder_2_isAtPosition(0)) {
            mechanism.cylinder_2_moveForward();
        }
        mechanism.cylinder_2_stop();
    }
}
