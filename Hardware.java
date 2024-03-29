import java.util.concurrent.*;

public class Hardware {

    static     {
        // C:\Users\Shazia\source\repos\Hardware\x64\Debug\Hardware.dll
        System.load("C:\\str2018\\Projects\\Hardware\\x64\\Debug\\Hardware.dll");
    }

    public int setBitValue(int var, int bit_n, boolean value) {
        int mask_on = 1<< bit_n;
        int mask_off = ~mask_on;
        if(value) var = var | mask_on;
        else var = var & mask_off;
        return var;
    }

    public boolean getBitValue(int var, int bit_n) {
        int result = var& (1<< bit_n) ;
        boolean bool_result = (result!=0);
        return    bool_result;
    }

    native public void create_di(int port);
    native public void create_do(int port);
    native private void write_port(int port, int value);
    native private int read_port(int port);


    private static final Semaphore  _critical_sem_= new Semaphore(1);

    public int SafeReadPort(int port) {
        return this.read_port(port);
    }

    public void SafeWritePort(int port, int value) {
        write_port(port, value);
        if (_critical_sem_.availablePermits() > 0) {  // this will be very handy
            System.out.println("DANGER: not handling critical area! Fix your code…");
            try {
                throw new Exception();
            }catch(Exception exception)    { // this fancy code is to show the class
                exception.printStackTrace(); // and line number of offending code
                java.awt.Toolkit.getDefaultToolkit().beep();
                System.exit(0);
            }
        }
    }

    static public void   enterCriticalArea()  {
        try { _critical_sem_.acquire(); } catch (InterruptedException ex) {}
    }

    static public void   leaveCriticalArea() {
        _critical_sem_.release();
    }

}
