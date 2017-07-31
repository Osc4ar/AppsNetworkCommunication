import java.io.*;
import java.net.*;
import java.util.Arrays;

public class Cliente extends Thread {
  int pto;
  int numbers[];

  public Cliente(int pto){
    this.pto=pto;
    System.out.print("Iniciando Cliente\n");

  }
  public void run(){
    try {
      ServerSocket ss = new ServerSocket(pto);
      Socket socket = ss.accept();
      System.out.print(socket+"\n\n");
      DataInputStream dis = new DataInputStream(socket.getInputStream());
      DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
      int elements = dis.readInt();
      System.out.println("Numero de elementos: "+elements);
      numbers = new int[elements];

      for (int i=0;i<elements ;i++ ) {
        numbers[i]=dis.readInt();
      }
      Arrays.sort(numbers);
      int recibido;
      for (int i : numbers) {
        dos.writeInt(i);
        dos.flush();
        System.out.println(""+i);
      }
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
}
