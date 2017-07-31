import javax.swing.JFileChooser;
import java.net.*;
import java.io.*;

public class CFiles {
  try {
    JFileChooser fc = new JFileChooser();
    int r = fc.showOpenDialog(null);
    if (r == JFileChooser.APPROVE_OPTION) {
      File f = fc.getSelectedFile();
      String name = f.getName();
      long size = f.getLength();
      long aux_size = size;
      DatagramSocket cl = new DatagramSocket();
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream(baos);
      long i = 0;
      while (aux_size > 0) {
        if () {

        }
        Data d = new Data(i, name, size, );
      }
      oos.writeObject(d);
      oos.flush();
      byte[] buf = boos.toByteArray();
      DatagramPacket p = new DatagramPacket();
    }
  } catch (Exception e) {
    e.printStackTrace();
  }
}
