import java.net.*;
import java.io.*;

public class S_ECO_D {
  public static void main(String[] args) {
    try {
      int pto = 7000;
      DatagramSocket s = new DatagramSocket(pto);
      System.out.println("Servidor iniciado esperando cliente");
      for (; ; ) {
        DatagramPacket p1 = new DatagramPacket(new byte[1500], 1500);
        s.receive(p1);
        String msj = new String(p1.getData(), 0, p1.getLength());
        if (msj.indexOf("salir") >= 0) {
          System.out.println("Termina aplicacion");
          s.close();
          System.exit(0);
        }
        System.out.println("Eco recibido desde: " + p1.getAddress() + ":" + p1.getPort() + " con el eco: " + msj);
        s.send(p1);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
