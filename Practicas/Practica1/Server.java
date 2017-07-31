import java.net.*;
import java.io.*;

public class Server {
  public static void main(String[] args) {
    try {
      int pto = 8000, porcentaje = 0;
      File myPath = new File(".");
      ServerSocket s = new ServerSocket(pto);
      System.out.println("Servidor iniciado...");
      for (; ; ) {
        Socket cl = s.accept();
        System.out.println("Cliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
        DataInputStream dis = new DataInputStream(cl.getInputStream());
        int numberFiles = dis.readInt();
        BufferedInputStream bis = new BufferedInputStream(cl.getInputStream());
        for (int i = 0; i < numberFiles; i++) {
          String name = dis.readUTF();
          long tam = dis.readLong();
          System.out.println("\nArchivo a recibir:\n\tNombre: " + name + "\n\tTamanio: " + tam);
          long recibidos = 0;
          BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myPath.getAbsolutePath().replace(".","") + name));
          while(recibidos < tam) {
            byte[] b = new byte[2000];
            int n = bis.read(b);
            recibidos += n;
            bos.write(b);
            porcentaje = (int)((recibidos*100)/tam);
            System.out.print("\r Enviado el " + porcentaje + "%");
          }
          bos.close();
        }
        System.out.println("\n\nEl cliente termino");
        dis.close();
        bis.close();
      }
    } catch(Exception e) {
      e.printStackTrace();
    }

  }
}
