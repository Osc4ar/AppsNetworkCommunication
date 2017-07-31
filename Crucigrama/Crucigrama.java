import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;
import java.util.TimerTask;

public class Crucigrama extends JFrame implements ActionListener {

  private JPanel jpControl, jpBoard;
  private JLabel nombre, puntuacion, jugadores, estado, tema, tiempo;
  private JButton[][] casilla;
  private JButton iniciarJuego;
  private String coord = "", coordToDea = "";
  private JComboBox jcbTemas;
  private String[] strTemas = {"Escuela", "Deportes", "Programacion", "Series", "Musica"};
  private boolean send=false;
  private String host = "127.0.0.1";
  private int pto = 5000, puntos, time;
  private Socket cl;
  private ObjectInputStream ois;
  private ObjectOutputStream oos;
  private DataInputStream dis;
  private DataOutputStream dos;
  private TimerTask lector;

  private int posx;
  private int posy;
  private String tablero;


  public Crucigrama() {
    super("Crucigrama");
    setLayout(new FlowLayout());
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(1000, 600);
    jpControl = new JPanel(new FlowLayout());
    jpBoard = new JPanel(new GridLayout(20, 20));
    casilla = new JButton[20][20];
    for (int i=0; i<20; i++) {
      for (int j=0;j<20 ;j++ ) {
        casilla[i][j] = new JButton("");
        casilla[i][j].addActionListener(this);
        jpBoard.add(casilla[i][j]);
      }
    }
    tema = new JLabel("Tema:");
    jpControl.add(tema);
    jcbTemas = new JComboBox(strTemas);
    jpControl.add(jcbTemas);
    iniciarJuego = new JButton("Jugar");
    iniciarJuego.addActionListener(this);
    jpControl.add(iniciarJuego);
    nombre = new JLabel("Nombre:");
    puntuacion = new JLabel("Puntuacion: 0");
    jugadores = new JLabel("# de jugadores: 1");
    estado = new JLabel("Esperando jugadores...");
    tiempo = new JLabel("Tiempo: 300s");
    jpControl.add(nombre);
    jpControl.add(puntuacion);
    jpControl.add(jugadores);
    jpControl.add(estado);
    jpControl.add(tiempo);
    add(jpControl);
    add(jpBoard);
    connection();
    setVisible(true);
  }

  public void index(JButton b) {
    for (int i = 0 ;i<20 ; i++) {
      for (int j=0;j<20 ; j++) {
        if (b==(JButton)casilla[i][j]) {
          posx = i;
          posy = j;
          break;
        }
      }
    }
  }
  public void connection() {
    try {
      cl = new Socket(host, pto);
      System.out.println("Conexion establecida");
      dis = new DataInputStream(cl.getInputStream());
      dos = new DataOutputStream(cl.getOutputStream());
      String chooseTopic = dis.readUTF();
      System.out.println(chooseTopic);
      if (chooseTopic.equals("0")) {
        jcbTemas.setEnabled(true);
        iniciarJuego.setEnabled(true);
      }
      else {
        jcbTemas.setEnabled(false);
        iniciarJuego.setEnabled(false);
        tablero = dis.readUTF();
        fill();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void initLector() {
    lector = new TimerTask() {
      public void run() {
        try {
          GameCount g = new GameCount(300);
          g.start();
          coordToDea = null;
          while(g.stillCounting()) {
            coordToDea = dis.readUTF();
            if (coordToDea != null) {
              unableButtons(coordToDea);
              coordToDea = null;
            }
            tiempo.setText("Tiempo: " + g.getTime() + "s");
            tiempo.updateUI();
          }
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    };
  }

  public void fill() {
    int k = 0;
    for (int i=0; i < 20; i++) {
      for (int j=0; j < 20; j++) {
        casilla[i][j].setText(""+tablero.charAt(k));
        k++;
      }
    }
  }

  public void unableButtons(String c) {
    String []coord = c.split("&");
    int x1 = Integer.parseInt(coord[0]);
    int y1 = Integer.parseInt(coord[1]);
    int x2 = Integer.parseInt(coord[2]);
    int y2 = Integer.parseInt(coord[3]);
    for (int i = x1; i <= x2; i++) {
      for (int j = y1; j <= y2; j++) {
        casilla[i][j].setEnabled(false);
      }
    }
  }

  public void actionPerformed(ActionEvent e) {
    JButton b = (JButton)e.getSource();
    if (b==iniciarJuego) {
      try {
        dos.writeUTF((String)jcbTemas.getSelectedItem());
        tablero = dis.readUTF();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      fill();
      iniciarJuego.setEnabled(false);
      jcbTemas.setEnabled(false);
    }
    else {
      index(b);
      if (!send) {
        coord = coord + posx + "&" + posy + "&";
        send = true;
      }
      else {
        coord = coord + posx + "&" + posy;
        System.out.println("Posicion: " + coord);
        send = false;
        try {
          dos.writeUTF(coord);
          System.out.println("Enviadas...");
          int nP = dis.readInt();
          if(nP != puntos) {
            puntos = nP;
            puntuacion.setText("Puntuacion: " + puntos);
            unableButtons(coord);
          }
          time = dis.readInt();
          tiempo.setText("Tiempo: " + time + "s");
          int nuevasCoord = dis.readInt();
          for (int i = 0; i < nuevasCoord; i++) {
            String coordRecibidas = "";
            coordRecibidas = dis.readUTF();
            unableButtons(coordRecibidas);
          }
        } catch(Exception ex) {
          ex.printStackTrace();
        }
        coord = "";
      }
    }
  }

  public static void main(String[] args) {
    new Crucigrama();
  }
}
