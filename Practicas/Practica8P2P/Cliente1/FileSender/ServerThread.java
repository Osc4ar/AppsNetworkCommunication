import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Arrays;

public class ServerThread extends Thread {
  int pto;
  ArrayList<Integer> ptos = new ArrayList<Integer>();
  ArrayList<String> allFiles = new ArrayList<String>();
  String []files;

  public ServerThread(int pto) {
    this.pto = pto;
  }

  public void run() {
    try {
      System.setProperty("java.net.preferIPv4Stack","true");
      MulticastSocket escucha = new MulticastSocket(55557);
      MulticastSocket escucha2 = new MulticastSocket(55558);
      System.out.println("Multicast");
      escucha.joinGroup(InetAddress.getByName("239.192.0.1"));
      escucha2.joinGroup(InetAddress.getByName("239.192.0.2"));
      byte [] dato = new byte [1024];
      byte [] datopto = new byte [1024];
      byte [] dato1pto = String.valueOf(pto).getBytes();
      DatagramPacket dgp = new DatagramPacket(dato, dato.length);
      DatagramPacket dgppto = new DatagramPacket(datopto, datopto.length);
      DatagramPacket dgp1;
      while (true) {
        Arrays.fill( dato, (byte) 0 );
        File folder = new File("./Data");
        String[] files = folder.list();
        for (String s : files) {
          byte [] dato1 = s.getBytes();
          dgp1 = new DatagramPacket(dato1, dato1.length, InetAddress.getByName("239.192.0.1"), 55557);
          escucha.send(dgp1);
        }
        escucha.receive(dgp);
        DatagramPacket dgp1pto = new DatagramPacket(dato1pto, dato1pto.length, InetAddress.getByName("239.192.0.2"), 55558);
        escucha2.send(dgp1pto);
        escucha2.receive(dgppto);
        String mensaje = new String(dato);
        String ptosend = new String(datopto);
        int ptoint= Math.round(Float.valueOf(ptosend));
        Iterator<Integer> it = ptos.iterator();
        int control = 0;
        while(it.hasNext()){
          if (it.next()==ptoint) {
            control++;
            break;
          }
        }
        if (control==0) {
          ptos.add(ptoint);
        }
        Iterator<Integer> ot = ptos.iterator();
        while(ot.hasNext()){
          System.out.println("Puerto: "+ot.next());
        }
        if (!allFiles.contains(mensaje)) {
          allFiles.add(mensaje);
          System.out.println("Nombre recibido\nArchivos:");
          for (String s : allFiles) {
            System.out.println("Archivo: " + s);
          }
        }
        System.out.println("recibido: " + ptosend);
        Thread.sleep(2000);
      }
    } catch (Exception e) {
        e.printStackTrace();
    }
  }

  public ArrayList<String> getAllFiles() {
    return allFiles;
  }
}
