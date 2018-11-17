public interface Cylinder {
    public void moveForward();
    public void moveBackward();
    public void stop();
    public void Goto(int position); // 0, 1
    public int getPosition();
}
