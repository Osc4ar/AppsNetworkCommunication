//axelernesto@gmail.com
import java.util.*;
import java.io.*;
import java.awt.*;
import java.net.*;

public class server {

    public static void main(String[] args) throws IOException {
    int portR = 4000;
    int portE = 4001;
    int chatsPrivados = 1;
    InetAddress group = null;
    try {
                System.setProperty("java.net.preferIPv4Stack","true");
                MulticastSocket socket = new MulticastSocket(portR);
                socket.setTimeToLive(100);
                group = InetAddress.getByName("227.1.1.1");
                socket.joinGroup(group);
                while(true) {
                  DatagramPacket dgram = new DatagramPacket(new byte[1500], 1500);
                  socket.receive(dgram); // Se bloquea hasta que llegue un datagrama
                  System.err.println("Recibido " + dgram.getLength() +
                    " bytes de " + dgram.getAddress() + " "+ new String(dgram.getData()));
                  String message = new String(dgram.getData());
                  if (message.indexOf("chat with ") != -1) {
                    String user1 = message.substring(2, message.indexOf(" )"));
                    String user2 = message.substring(message.indexOf("chat with ") + 10, message.length());
                    chatsPrivados++;
                    new cliente(user1, user2, 4000 + chatsPrivados, 4001 + chatsPrivados);
                    new cliente(user2, user1, 4001 + chatsPrivados, 4000 + chatsPrivados);
                    chatsPrivados++;
                  }
                  String addr = "" + dgram.getAddress();
                  System.out.println(message);
                  byte[] msj = message.getBytes();
                  DatagramPacket tosend = new DatagramPacket(msj, msj.length, group, portE);
                  socket.send(tosend);
                }

        } catch (Exception e) {
            e.printStackTrace();
    }

  }

}
