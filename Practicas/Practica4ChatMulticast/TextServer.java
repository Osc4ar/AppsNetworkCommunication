import java.io.*;
import java.net.*;

public class TextServer extends Thread {

  public static final String address = "230.0.0.1";
  public static final int port = 9013;
  public static final int buffLen = 512;

  public void run() {
    String localH = "";
    String message = "";
    InetAddress group = null;
    try {
      localH = InetAddress.getLocalHost().getHostAddress();
      group = InetAddress.getByName(address);
    } catch (UnknownHostException e) {
      e.printStackTrace();
      System.exit(1);
    }
    for (; ; ) {
      try {
        MulticastSocket socket = new MulticastSocket(port);
        socket.joinGroup(group);
        byte[] buffer = new byte[buffLen];
        DatagramPacket recv = new DatagramPacket(buffer, buffer.length);
        socket.receive(recv);
        message += recv.getAddress() + ": ";
        byte[] data = recv.getData();
        message += new String(data);
        message += "\n";
        DatagramPacket tosend = new DatagramPacket(message.getBytes(), message.length(), group, port);
        System.out.println(message);
        socket.send(tosend);
      } catch (Exception e) {
        e.printStackTrace();
        System.exit(2);
      }
    }
  }
}
