import java.net.*;
import java.io.*;

public class cliente{
  public static void main(String[] args) {

    try {
      InetAddress dir = null;
      String host = "";
      int pto = 5555;
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      while (true) {
        System.out.println("Escribe la direccion del servidor.");
        host = br.readLine();

          try {
            dir = InetAddress.getByName(host);

          } catch (UnknownHostException e) {
            System.err.println("direccion no valida");
            continue;
          }
        break;
      }
      Socket cl = new Socket(dir,pto);
      System.out.println("Conexion establecida\n\nWelcome to the LolSQL monitor.\nCommands end with ;\nYour MySQL connection id is 6\nServer version: 5.7.16 MySQL Community Server (GPL)\n\n");
      PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
      BufferedReader br1 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
      String msj = "";
      String aux = "";
      for (; ; ) {

        while (aux.indexOf(';') == -1) {
          System.out.print("lolsql> ");
          msj = br.readLine();
          aux = aux + msj;
          if (aux.indexOf(';') != -1) {
            break;
          }
        }

        pw.println(aux);
        aux="";
        pw.flush();
        if (msj.compareToIgnoreCase("bye")==0) {
          System.out.println("Bye");
          br1.close();
          br.close();
          pw.close();
          cl.close();
          break;
        } else {
          String respuesta = br1.readLine();
          String[] lines = respuesta.split("&");
          for (String line : lines)
            System.out.println(line);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
