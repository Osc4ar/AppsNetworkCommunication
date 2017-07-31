import java.net.*;
import java.io.*;
import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Arrays;

public class Client extends JFrame implements ActionListener {

  private JButton addFiles, sendFiles;
  private JTextField selectHost;
  private JLabel hostLabel;
  private JList filesList;
  private DefaultListModel model;
  private final int pto = 8000;
  private String host;
  private JFileChooser fc;
  private DataOutputStream dos;
  private Socket cl;
  private ArrayList<File> filesAList;

  public Client() {
    super("File Transfer Client");
    setSize(350, 360);
    setLayout(new FlowLayout());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    hostLabel = new JLabel("Host:");
    add(hostLabel);
    selectHost = new JTextField(20);
    add(selectHost);
    addFiles = new JButton("Add Files");
    addFiles.addActionListener(this);
    add(addFiles);
    sendFiles = new JButton("Send Files");
    sendFiles.addActionListener(this);
    add(sendFiles);
    model = new DefaultListModel();
    filesList = new JList(model);
    filesList.setLayoutOrientation(JList.VERTICAL);
    JScrollPane listScroller = new JScrollPane(filesList);
    listScroller.setPreferredSize(new Dimension(300, 250));
    add(listScroller);
    filesAList = new ArrayList<File>();
    fc = new JFileChooser();
    fc.setMultiSelectionEnabled(true);
    new FileDrop(this, new FileDrop.Listener() {
      public void filesDropped(File[] files) {
        model.clear();
        updateList(files);
      }
    });
    setVisible(true);
  }
  public void updateList(File[] files) {
    filesAList.addAll(Arrays.asList(files));
    for (File f: filesAList)
      model.addElement(f.getName());
  }

  public void actionPerformed(ActionEvent ev) {
    if (ev.getSource() == addFiles) {
      int r = fc.showOpenDialog(this);
      if (r == JFileChooser.APPROVE_OPTION)
        updateList(fc.getSelectedFiles());
    }
    if (ev.getSource() == sendFiles) {
      try {
        int porcentaje = 0;
        String host = selectHost.getText();
        Socket cl = new Socket(host, pto);
        dos = new DataOutputStream(cl.getOutputStream());
        dos.flush();
        dos.writeInt(filesAList.size());
        dos.flush();
        for (File f: filesAList) {
          String nombre = f.getName();
          String path = f.getAbsolutePath();
          long tam = f.length();
          dos.writeUTF(nombre);
          dos.flush();
          dos.writeLong(tam);
          dos.flush();
          DataInputStream dis = new DataInputStream(new FileInputStream(path));
          long enviados = 0;
          while (enviados < tam) {
            byte[] b = new byte[2000];
            int n = dis.read(b);
            enviados = enviados + n;
            dos.write(b, 0, n);
            dos.flush();
            porcentaje = (int) ((enviados*100)/tam);
            System.out.print("\r Enviado el " + porcentaje + "%");
          }
          System.out.println("\n Archivo enviado...");
          dis.close();
        }
        dos.close();
        cl.close();
        filesAList.clear();
        model.clear();
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
  }

  public static void main(String[] args) {
    new Client();
  }
}
