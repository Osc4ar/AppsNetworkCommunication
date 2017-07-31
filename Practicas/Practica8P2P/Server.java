import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.rmi.server.*;
import java.rmi.*;

public class Server extends Thread {

  private int port = 55557;
  private String address = "239.129.0.1", username;
  private ArrayList<String> myArrListFiles;
  private ArrayList<String> arrListFiles;
  private MulticastSocket multiSocket;
  private byte[] data;
  private DatagramPacket dPacket;
  private File dataFolder;
  private ArrayList<String> peers;
  private String askedFile;

  public Server(String username) {
    try {
      System.setProperty("java.net.preferIPv4Stack", "true");
      multiSocket = new MulticastSocket(port);
      multiSocket.joinGroup(InetAddress.getByName(address));
      data = new byte[1024];
      dPacket = new DatagramPacket(data, data.length);
      dataFolder = new File("./Data");
      myArrListFiles = new ArrayList<String>();
      arrListFiles = new ArrayList<String>();
      peers = new ArrayList<String>();
      this.username = username;
      askedFile = "";
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void run() {
    try {
      while (true) {
        Thread.sleep(1000);
        updateFiles();
        sendFilesList();
        arrListFiles.addAll(myArrListFiles);
        for (int i = 0; i < 15; i++) {
          receiveFileList();
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void sendFilesList() {
    try {
      DatagramPacket toSend;
      for (String f : myArrListFiles) {
        byte[] fData = f.getBytes();
        toSend = new DatagramPacket(fData, fData.length, InetAddress.getByName(address), port);
        multiSocket.send(toSend);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void receiveFileList() {
    try {
      boolean add = true;
      Arrays.fill(data, (byte)0);
      multiSocket.receive(dPacket);
      String received = new String(data);
      String cleaned = "";
      for (int i = 0; i < received.length(); i++) {
        if ((int)received.charAt(i) != 0)
          cleaned += received.charAt(i);
      }
      if (cleaned.charAt(0) == '<') {
        add = false;
        sendConfirmation(cleaned.substring(1));
      }
      if (cleaned.charAt(0) == '>') {
        add = false;
        if (askedFile.equals(cleaned.substring(cleaned.indexOf("<") + 1))) {
          peers.add(cleaned.substring(1, cleaned.indexOf("<")));
          System.out.println("Active peers: " + peers.size());
          for (String user : peers) {
            System.out.println("User: " + user);
          }
        }
      }
      if (!arrListFiles.contains(cleaned) && add) {
        arrListFiles.add(cleaned);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendConfirmation(String fName) {
    if (myArrListFiles.contains(fName)) {
      try {
        DatagramPacket toSend;
        byte[] fData = (">" + username + "<" + fName).getBytes();
        toSend = new DatagramPacket(fData, fData.length, InetAddress.getByName(address), port);
        multiSocket.send(toSend);
        ExchangeFiles ex = new ExchangeFiles(fName);
        Naming.rebind(username, ex);
        System.out.println("Server: " + username + "started");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void updateFiles() {
    for (String s : dataFolder.list()) {
      if (!myArrListFiles.contains(s))
        myArrListFiles.add(s);
    }
  }

  public ArrayList<String> getArrListFiles() {
    return arrListFiles;
  }

  public void askFile(String fName) {
    try {
      askedFile = fName;
      peers.clear();
      DatagramPacket toSend;
      byte[] fData = ("<" + fName).getBytes();
      toSend = new DatagramPacket(fData, fData.length, InetAddress.getByName(address), port);
      multiSocket.send(toSend);
      Thread.sleep(1500);
      
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
