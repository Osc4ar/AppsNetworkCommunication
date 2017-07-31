import java.io.*;
import java.net.*;

public class ServerO {
  public static void main(String[] args) {
    try {
      int pto = 8888;
      ServerSocket s = new ServerSocket(pto);
      System.out.println("Servidor iniciado...");
      for (; ; ) {
        Socket cl = s.accept();
        System.out.println("Cliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
        ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
        ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
        Objeto o = (Objeto)ois.readObject();
        System.out.println("Se recibio objeto con los datos:\nNombre: " + o.nombre + "\nApellido pat: " +
        o.apaterno + "\nApellido mat: " + o.amaterno + "\nEdad: " + o.edad + "\nClave: " + o.clave);
        o.nombre = o.nombre + "ECO";
        o.apaterno = o.apaterno + "ECO";
        o.amaterno = o.amaterno + "ECO";
        oos.writeObject(o);
        oos.flush();
        System.out.println("Se envio objeto con los datos:\nNombre: " + o.nombre + "\nApellido pat: " +
        o.apaterno + "\nApellido mat: " + o.amaterno + "\nEdad: " + o.edad + "\nClave: " + o.clave);
        oos.close();
        ois.close();
        cl.close();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
