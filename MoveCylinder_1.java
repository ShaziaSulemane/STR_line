public class MoveCylinder_1 extends Thread {
    private Mechanism mechanism;
    public MoveCylinder_1 (Mechanism mechanism) {
        this.mechanism = mechanism;
    }

    public void run () {
        try {
            while (true) {
                while ((Integer) Main.taskActive.peek() == 1) {
                    mechanism.cylinder_1_Goto(1);
                    mechanism.cylinder_1_Goto(0);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
