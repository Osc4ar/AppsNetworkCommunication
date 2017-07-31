import java.net.*;
import java.io.*;

public class S_HM_D {
  public static void main(String[] args) {
    try {
      int pto = 9000;
      DatagramSocket s = new DatagramSocket(pto);
      System.out.println("Servicio iniciado esperando mensajes");
      for (; ; ) {
        DatagramPacket p = new DatagramPacket(new byte[1500], 1500);
        s.receive(p);
        System.out.println("Mensaje recibido desde: " + p.getInetAddress() + ":" + p.getPort());
        String datos = new String(p.getData(), , p.getLength());
        s.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
