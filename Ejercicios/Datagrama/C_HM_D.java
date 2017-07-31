import java.net.*;
import java.io.*;
// Hace cliente ECO con hasta 65535 bytes b[:65535]
public class C_HM_D {
  public static void main(String[] args) {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      int pto = 9000;
      System.out.println("Escribe la direccion del servidor:");
      String host = br.readLine();
      System.out.println("Escribe un mensaje:");
      String msj = br.readLine();
      byte[] b = msj.getBytes();
      DatagramSocket cl = new DatagramSocket();
      System.out.println("Enviando mensaje...");
      DatagramPacket p = new DatagramPacket(b, b.length, InetAddress.getByName(host), pto);
      cl.send(p);
      System.out.println("Mensaje enviado");
      cl.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
