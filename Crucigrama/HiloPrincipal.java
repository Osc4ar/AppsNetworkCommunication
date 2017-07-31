import javax.swing.*;
import java.awt.event.*;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class HiloPrincipal extends HilosClientes {

  int posx,posy;
  private char[][] casilla;
  private String[] strTemas = {"Escuela", "Deportes", "Programacion", "Series", "Musica"};
  private String[] escuela = {"ALUMNO", "MAESTRO", "LAPIZ", "BORRADOR", "LIBRETA","BUTACA","PLUMA","ESTUCHERA","MOCHILA","LIBRO","CAFETERIA","CALCULADORA","PIZARRON","DIPLOMA"};
  private String[] deportes = {"NATACION","BASKETBALL","AMERICANO","RAQUETA","BALON","ARBITRO","LUCHALIBRE","GIMNASIA","BASEBALL","CICLISMO","FCPOLITECNICO","AGUILASBLANCAS","BUHOSVERDES"};
  private String[] programacion = {"ORIENTADOAOBJETOS","JAVA","PYTHON","ESTRUCTURADEDATOS","DATO","INSTANCIA","CLASE","GOLANG","DJANGO","VHDL","SWIFT","ABSTRACCION","ENCAPSULAMIENTO"};
  private String[] series = {"ARROW","ESCENOGRAFIA","THIRTEENREASONSWHY","HOWIMETYOURMOTHER","TWENTYFOUR","SIMPSONS","GLEE","LAREYNADELSUR","GUION","DIRECTOR","DREKYJOSH","SOYLUNA","ACTORES"};
  private String[] musica = {"CANCION","PISTA","ROCK","PUNK","ALTERNATIVE","R&B","ELECTRO","HOUSE","REGGAETON","BALADAS","POP","RICARDOARJONA","GUITARRA","BAJO","PIANO","BATERIA"};

  public HiloPrincipal(Socket socket, String str) {
    super(socket, str);
  }

  public void run() {
    try {
      System.out.println("Hilo Principal");
      dis = new DataInputStream(socket.getInputStream());
      dos = new DataOutputStream(socket.getOutputStream());
      dos.writeUTF("0");
      tablero = dis.readUTF();
      System.out.println(tablero);
      fill(tablero);
      enviaTablero();
      while(!getJTerminado()) {
        readMoves();
        waitResult();
      }
      System.out.println("Terminado");
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public boolean CheckBlank1(String word, int x, int y) {
    for (int i = 0; i < word.length(); i++) {
      String res = "" +casilla[i+y][i+x];
      if (!res.equals("_") && !res.equals(""+word.charAt(i)) ) {
        return false;
      }
    }
    return true;
  }
  public boolean CheckBlank2(String word,int x,int y){
    for (int i=0; i<word.length(); i++) {
      String res = "" + casilla[y][i+x];
      if (!res.equals("_") && !res.equals(""+word.charAt(i))) {
        return false;
      }
    }
    return true;
  }
  public boolean CheckBlank3(String word,int x,int y){
    for (int i=0; i<word.length(); i++) {
      String res = "" + casilla[y+i][x];
      if (!res.equals("_") && !res.equals(""+word.charAt(i))) {
        return false;
      }
    }
    return true;
  }
  public boolean CheckBlank4(String word,int x,int y){
    for (int i=0; i<word.length(); i++) {
      String res = "" + casilla[y+i][x-i];
      if (!res.equals("_") && !res.equals(""+word.charAt(i))) {
        return false;
      }
    }
    return true;
  }
  public void fill_word(String word){
    int x = (int)(Math.random()*20);
    int y = (int)(Math.random()*20);
    if (x + word.length() < 20 && CheckBlank2(word, x, y) == true ) {
      int z;
      String cord;
      cord = y+"&"+x+"&";
      for (z= 0;z<word.length();z++) {
        casilla[y][z+x] = (word.charAt(z));
      }
      cord = cord + y +"&"+ (word.length()-1+x);
      coordenadas.add(cord);
    }
    else if (y+word.length() < 20 &&  x+word.length() < 20 && CheckBlank1(word, x, y) == true ) {
      int z;
      String cord;
      cord = y+"&"+x+"&";
      for ( z = 0;z<word.length();z++ ) {
        casilla[z+y][z+x] = (word.charAt(z));
      }
      cord = cord + (word.length()-1+y) +"&"+ (word.length()-1+x);
      coordenadas.add(cord);
    }
    else if (y+word.length()<20 && CheckBlank3(word,x,y)==true) {
      int z;
      String cord;
      cord = y+"&"+x+"&";
      for ( z = 0;z<word.length();z++ ) {
        casilla[z+y][x] = (word.charAt(z));
      }
      cord = cord + (word.length()-1+y) +"&"+ (x);
      coordenadas.add(cord);
    }
    else if (y+word.length()<20 &&  x-word.length()>0 && CheckBlank4(word,x,y)==true ) {
      int z;
      String cord;
      cord = y+"&"+x+"&";
      for ( z = 0;z<word.length();z++ ) {
        casilla[word.length()-1+y][x-z]= (word.charAt(z));
      }
      cord = cord + (word.length()-1+y) +"&"+ (x-(word.length()-1));
      coordenadas.add(cord);
    }
    else {
      fill_word(word);
    }
  }
  public void fill(String categoria) {
      tablero = "";
      casilla = new char[20][20];
      for (int i=0; i<20; i++) {
        for (int j=0; j<20; j++) {
          casilla[i][j]='_';
        }
      }
      String[] arreglo = new String[20];
      if (categoria.equals("Escuela")) {
  		       arreglo = escuela;
      }
      if (categoria.equals("Deportes")) {
  		       arreglo = deportes;
      }
      if (categoria.equals("Programacion")) {
  		       arreglo = programacion;
      }
      if (categoria.equals("Series")) {
  		       arreglo = series;
      }
      if (categoria.equals("Musica")) {
  		       arreglo = musica;
      }
      for (int i=0; i < arreglo.length; i++) {
        fill_word(arreglo[i]);
      }
      for (int i = 0; i < 20; i++) {
        for (int j = 0; j < 20; j++) {
          int ran = (int)(Math.random()*25) + 65;
          char caracter = (char)ran;
          if (casilla[i][j] == '_') {
            casilla[i][j] = caracter;
          }
          tablero = tablero + casilla[i][j];
        }
      }
  }

  public void index(char b){
    for (int i = 0 ;i<20 ; i++) {
      for (int j=0;j<20 ; j++) {
        if (b==casilla[i][j]) {
          posx = i;
          posy = j;
          break;
        }
      }
    }
  }
}
