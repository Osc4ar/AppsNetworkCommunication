import java.io.*;
import java.net.*;
import java.util.Scanner;

public class S_Principal  {
  int []numbers;
  int buckets, pto = 9000;
  ServerSocket ssmain;
  int i;

  public S_Principal(){
    numbers = new int [3500];
    mainConnection(ssmain);
  }
  public void mainConnection(ServerSocket ss){
    try{
      ss = new ServerSocket(8999);
      System.out.println(ss.getInetAddress());
      System.out.println(ss);
      ss.setReuseAddress(true);
      System.out.println("Servicio Iniciado...\n\nEsperando Cliente\n\n");
      Socket socket = ss.accept();
      System.out.println(socket);
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      buckets = dis.readInt();
      System.out.println("Se usaran "+buckets+" Cubetas");
      dos.writeUTF("OK");
      Connection(buckets);
    }catch (EOFException e) {
      e.printStackTrace();
    }catch (IOException ex) {
      ex.printStackTrace();
    }
  }
  public void Connection(int n){
    for (i=0; i<n; i++, pto++) {
      new Cliente(pto).start();
    }
  }
  public static void main(String[] args) {
    S_Principal s = new S_Principal();
  }
}
