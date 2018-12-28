public class CalibrateCylinder3 extends Thread {
    Mechanism mechanism;

    public CalibrateCylinder3(Mechanism mechanism){
        this.mechanism = mechanism;
    }

    public void run () {
        while (!mechanism.cylinder_3_isAtPosition(0)) {
            mechanism.cylinder_3_moveForward();
        }
        mechanism.cylinder_3_stop();
    }
}
