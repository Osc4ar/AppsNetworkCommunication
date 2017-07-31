import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;

public class HiloSecundario extends HilosClientes {

  public HiloSecundario(Socket socket, String str){
    super(socket, str);
  }

  public void run() {
    try {
      System.out.println("Hilo Secundario");
      dis = new DataInputStream(socket.getInputStream());
      dos = new DataOutputStream(socket.getOutputStream());
      dos.writeUTF("1");
      enviaTablero();
      while(!getJTerminado()) {
        readMoves();
        waitResult();
      }
      System.out.println("Terminado");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
