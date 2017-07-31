import java.io.*;
import java.net.*;

public class EnviaO {
  public static void main(String[] args) {
    try {
      String host = "127.0.0.1";
      int pto = 8888;
      Socket cl = new Socket(host, pto);
      System.out.println("Conexion establecida enviando objeto");
      ObjectOutputStream oos = new ObjectOutputStream(cl.getOutputStream());
      ObjectInputStream ois = new ObjectInputStream(cl.getInputStream());
      Objeto o = new Objeto("Pepe", "Suarez", "Gonzales", 20, 1234);
      oos.writeObject(o);
      oos.flush();
      System.out.println("Se envio objeto con los datos:\nNombre: " + o.nombre + "\nApellido pat: " +
      o.apaterno + "\nApellido mat: " + o.amaterno + "\nEdad: " + o.edad + "\nClave: " + o.clave);
      Objeto o1 = (Objeto) ois.readObject();
      System.out.println("Se recibio objeto con los datos:\nNombre: " + o1.nombre + "\nApellido pat: " +
      o1.apaterno + "\nApellido mat: " + o1.amaterno + "\nEdad: " + o1.edad + "\nClave: " + o1.clave);
      oos.close();
      ois.close();
      cl.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
