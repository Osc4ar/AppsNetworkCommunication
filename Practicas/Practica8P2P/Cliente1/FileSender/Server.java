import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server implements Runnable {
  int pto;
  ServerSocket s;
  Socket cl;
  DataInputStream dis;
  DataOutputStream dos;
  ObjectOutputStream oos;
  ServerThread sthr;
  ArrayList<String> allFiles;

  public Server(int pto){
    System.setProperty("java.net.preferIPv4Stack","true");
    this.pto = pto;
    allFiles = new ArrayList<String>();
  }
  public void run() {
    connectLocal();
    connectMulticast();
    while (true) {
      if (allFiles.size() != sthr.getAllFiles().size()) {
        System.out.println("Clase server tiene ArrayList");
        allFiles = sthr.getAllFiles();
        sendAllFilesList();
        System.out.println("Enviado el ArrayList");
      }
    }
  }

  public void connectMulticast() {
    sthr = new ServerThread(pto);
    sthr.start();
  }

  public void connectLocal() {
    try {
      s = new ServerSocket(pto);
      cl = s.accept();
      System.out.println("Conexion establecida con clase Cliente");
      dis = new DataInputStream(cl.getInputStream());
      dos = new DataOutputStream(cl.getOutputStream());
      oos = new ObjectOutputStream(cl.getOutputStream());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendAllFilesList() {
    try {
      oos.writeObject(allFiles);
      oos.flush();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
