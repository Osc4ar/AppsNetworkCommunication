import java.util.*;
import java.io.*;
import java.awt.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

public class cliente extends JFrame implements java.io.Serializable,ActionListener{

  JEditorPane  p;
  Timer time = new Timer();
  JTextArea jta;
  JFrame user;
  JButton enviar;
  JButton archivo;
  JPanel p1;
  JPanel p2;
  MulticastSocket s;
  int portE;
  int portR;
  int i=0;
  JLabel usuario;
  String us;
  String fmsj,aux;
  InetAddress group = null;

  public cliente(String Usuario){
    super("Chat publico");
    portE = 4000;
    portR = 4001;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GridLayout grid = new GridLayout(3,1);
    p1 = new JPanel();
    p2 = new JPanel();
    us = Usuario;
    usuario = new JLabel("Usuario:" + us);
    p1.setLayout(grid);
    p = new JEditorPane();
    p.setContentType("text/html");
    p.setEditable(false);
    p.setText(" \n:)");
    p.setBounds(0,0,300,300);
    JScrollPane scroll = new JScrollPane (p,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroll.setBounds(20,20,300,300);
    jta = new JTextArea("Ingresa Mensaje ...");
    jta.setBounds(0,0,300,10);
    jta.setLineWrap(true);
    enviar = new JButton("Enviar");
    archivo = new JButton("Archivo");
    p1.add(usuario);
    p1.add(scroll);
    p2.add(jta);
    p2.add(enviar);
    p2.add(archivo);
    p1.add(p2);
    add(p1);
    enviar.addActionListener(this);
    archivo.addActionListener(this);
    setSize(600,400);
    setVisible(true);
    try{
      System.setProperty("java.net.preferIPv4Stack","true");
      group = InetAddress.getByName("227.1.1.1");
      s = new MulticastSocket(portR);
      s.setTimeToLive(100);
      s.joinGroup(group);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  public cliente(String Usuario, String user2, int puertoE, int puertoR) {
    super("Chat privado de " + Usuario + " y " + user2);
    portE = puertoE;
    portR = puertoR;
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    GridLayout grid = new GridLayout(3,1);
    p1 = new JPanel();
    p2 = new JPanel();
    us = Usuario;
    usuario = new JLabel("Usuario:" + us);
    p1.setLayout(grid);
    p = new JEditorPane();
    p.setContentType("text/html");
    p.setEditable(false);
    p.setText(" \n:)");
    p.setBounds(0,0,300,300);
    JScrollPane scroll = new JScrollPane (p,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
    scroll.setBounds(20,20,300,300);
    jta = new JTextArea("Ingresa Mensaje ...");
    jta.setBounds(0,0,300,10);
    jta.setLineWrap(true);
    enviar = new JButton("Enviar");
    archivo = new JButton("Archivo");
    p1.add(usuario);
    p1.add(scroll);
    p2.add(jta);
    p2.add(enviar);
    p2.add(archivo);
    p1.add(p2);
    add(p1);
    enviar.addActionListener(this);
    archivo.addActionListener(this);
    setSize(600,400);
    setVisible(true);
    try{
      System.setProperty("java.net.preferIPv4Stack","true");
      group = InetAddress.getByName("227.1.1.1");
      s = new MulticastSocket(portR);
      s.setTimeToLive(100);
      s.joinGroup(group);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
  TimerTask tiempo = new TimerTask(){
            public void run(){
              try {
                DatagramPacket pa = new DatagramPacket(new byte[1500],1500);
                s.receive(pa);
                String data = new String(pa.getData(),0,pa.getLength());
                if(fmsj == null){
                  fmsj = data;
                }
                else{
                  fmsj = fmsj +"<br>"+ data;
                }
                jta.setText("");
                p.setText(fmsj);
              }catch (Exception e) {
                e.printStackTrace();
              }
    }
 };
 public void corriendo() {
    try {
         time.schedule(tiempo, 100, 100);
    } catch(Exception e) {
    }
   }

  public void actionPerformed (ActionEvent e){
      JButton b = (JButton)e.getSource();

      if (b==enviar) {
        try {
          aux = jta.getText();
          if (aux.contains(":D")) {
            aux = aux.replaceAll(":D","<IMG SRC=http://imagenpng.com/wp-content/uploads/2015/03/Carita-Feliz-PNG-1.png WIDTH=30 HEIGHT=30>");
          }
          if (aux.contains(":P")) {
            aux = aux.replaceAll(":P","<IMG SRC=https://s3.amazonaws.com/gex.lifeandstyle/uploads/image/file/2352/WinkEmoji.png WIDTH=30 HEIGHT=30>");
          }
          if (aux.contains("8D")) {
            aux = aux.replaceAll("8D","<IMG SRC=https://s-media-cache-ak0.pinimg.com/736x/8c/78/81/8c7881e03f35914fed799bde94b75927.jpg WIDTH=30 HEIGHT=30>");
          }
          if (aux.contains(":'D")) {
            aux = aux.replaceAll(":'D","<IMG SRC=http://www.movilescelular.com/wp-content/uploads/2016/07/Emoji-Risa.jpg WIDTH=50 HEIGHT=30>");
          }
          byte[] msj = ("( "+us+" ):  "+ aux).getBytes();
          DatagramPacket dgram = new DatagramPacket(msj, msj.length,InetAddress.getByName("227.1.1.1"), portE);
          s.send(dgram);

        }catch (Exception a) {
          a.printStackTrace();
        }
      }
      if (b==archivo){
        System.out.println("1");
      }
      corriendo();
  }
}
