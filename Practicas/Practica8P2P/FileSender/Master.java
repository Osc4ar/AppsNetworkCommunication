import java.util.*;
import java.lang.*;

public class Master {
  Server s;
  Cliente c ;
  int pto;
  public Master(int pto) {
    System.setProperty("java.net.preferIPv4Stack","true");
    this.pto = pto;
  }
  public void connect() {
    Runnable c = new Cliente(pto);
    Runnable s = new Server(pto);
    new Thread(s).start();
    new Thread(c).start();
  }
  public static void main(String[] args) {
    new Master(Integer.parseInt(args[0])).connect();
  }
}
