import java.io.*;
import java.net.*;

public class ServerOut extends Thread {
  int pto;
  String dir = "127.0.0.1";
  public ServerOut(int pto){
  this.pto = pto;
  }
  public void run(){
    try {
      Socket cl = new Socket(dir, pto);
      DataOutputStream dos = new DataOutputStream(cl.getOutputStream());
      DataInputStream in = new DataInputStream(cl.getInputStream());
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
