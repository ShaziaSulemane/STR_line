package LabWork2;

public class Main {

    public static void main(String []args){
        Hardware h = new Hardware();
        h.create_di(0);
        h.create_di(1);
        h.create_do(2);

        Cylinder cyl1= new Cylinder_1(h);
        Cylinder cyl2= new Cylinder_2(h);
        Cylinder cyl3= new Cylinder_3(h);

        Mechanism mechanism = new Mechanism(h, cyl1, cyl2, cyl3);
        ThreadIdentify threadIdentify = new ThreadIdentify(mechanism);
        threadIdentify.start();
        mechanism.MoveConveyor();
        //mechanism.identifyPart();  comment blocking function
        int t=0;
        while(true) {  // e--> exit
            ThreadIdentify.startIdentification();  // then, start it from a thread
            mechanism.cylinder_1_Goto(1);
            mechanism.cylinder_1_Goto(0);
            Integer partType = ThreadIdentify.TryConsumeMessage();
            System.out.printf("\n part is %d", partType);
            //try{ t=System.in.read(); } catch(Exception e){ }
        }
    }
}


