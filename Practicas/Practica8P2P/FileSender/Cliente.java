import java.io.*;
import javax.swing.*;
import java.net.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import java.util.ArrayList;

public class Cliente extends JFrame implements ActionListener, Runnable {

  JComboBox recursos;
  JButton descargar;
  JButton salir;
  JPanel prin;
  JLabel estado;
  String dir = "127.0.0.1";
  int pto;
  ArrayList<ServerOut> servidores = new ArrayList<>();
  ArrayList<String> allFiles;
  DataOutputStream dos;
  DataInputStream dis;
  ObjectInputStream ois;
  DefaultComboBoxModel model;
  Socket cl;
  public Cliente(int pto) {
    super("Servicio de Descarga de Archivos");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    prin = new JPanel(new FlowLayout());
    model = new DefaultComboBoxModel();
    recursos = new JComboBox(model);
    descargar = new JButton("Descargar");
    salir = new JButton("Salir");
    estado = new JLabel("Esperando...");
    prin.add(recursos);
    prin.add(descargar);
    prin.add(salir);
    prin.add(estado);
    descargar.addActionListener(this);
    salir.addActionListener(this);
    add(prin);
    setSize(300,100);
    setVisible(true);
    this.pto = pto;
  }
  public void run(){
    connectLocal();
    while (true) {
      try {
        allFiles = (ArrayList<String>)ois.readObject();
        System.out.println("Clase cliente recibio ArrayList");
      } catch (Exception e) {
        e.printStackTrace();
      }
      model.removeAllElements();
      for (String f : allFiles)
        model.addElement(f);
      recursos.setModel(model);
    }
  }
  public void connectLocal(){
    try {
       cl = new Socket(dir, pto);
       dos = new DataOutputStream(cl.getOutputStream());
       dis = new DataInputStream(cl.getInputStream());
       ois = new ObjectInputStream(cl.getInputStream());
       estado.setText("Conectado al Server");
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void newServer(int pto){
    servidores.add(new ServerOut(pto));
  }
  public void RequestFile(String file){
    try {
      dos.writeUTF(file);
    }catch (Exception e) {
      e.printStackTrace();
    }
  }
  public void actionPerformed(ActionEvent e){
    JButton b = (JButton)e.getSource();
    try {
      if (b == descargar) {
        RequestFile(String.valueOf(recursos.getSelectedItem()));
      }
      if (b == salir) {
          dos.close();
          cl.close();
      }
    }catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
