import java.net.*;
import java.io.*;

public class C_ECO {
  public static void main(String[] args) {
    try {
      InetAddress dir = null;
      String host = "";
      int pto = 5000;
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      while (true) {
        System.out.println("Escribe la direccion del servidor.");
        host = br.readLine();
        try {
          dir = InetAddress.getByName(host);
        } catch (UnknownHostException u) {
          System.err.println("Dir. no valida\n");
          continue;
        }
        break;
      }
      Socket cl = new Socket(dir, pto);
      System.out.println("Conexion establecida \n Escribe un mensaje, <Enter> para enviar, \"Salir\" para terminar:");
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
      BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
      String msj = "";
      for (; ; ) {
        msj = br.readLine();
        pw.println(msj);
        pw.flush();
        if (msj.compareToIgnoreCase("salir") == 0) {
          System.out.println("Termina aplicacion");
          br1.close();
          br.close();
          pw.close();
          cl.close();
          System.exit(0);
        } else {
          String eco = br1.readLine();
          System.out.println("Eco recibido: " + eco);
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
