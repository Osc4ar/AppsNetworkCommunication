import java.net.*;
import java.io.*;

public class Server implements Runnable{
  int pto;
  ServerSocket s;
  Socket cl;
  DataInputStream dis;
  DataOutputStream dos;
  public Server(int pto){
  System.setProperty("java.net.preferIPv4Stack","true");
  this.pto=pto;
  }
  public void run(){
     connectLocal();
     connectMulticast();
  }
  public void connectMulticast(){
      ServerThread sthr = new ServerThread(pto);
      sthr.start();
  }
  public void connectLocal(){
    try {
      s = new ServerSocket(pto);
      cl = s.accept();
      System.out.println("Conexión Establecida");
      dis = new DataInputStream(cl.getInputStream());
      dos = new DataOutputStream(cl.getOutputStream());
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
