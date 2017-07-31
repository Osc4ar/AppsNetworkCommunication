import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class HilosClientes extends Thread {

  public Socket socket;
  public String tablero;
  public DataOutputStream dos;
  public DataInputStream dis;
  public int puntaje, time;
  public String coordMovs;
  public boolean juegoTerminado, newMov, cont;
  public ArrayList<String> coordenadas = new ArrayList<String>();

  public HilosClientes(Socket socket, String tablero){
    this.socket = socket;
    this.tablero = tablero;
    cont = false;
    puntaje = 0;
    time = 300;
    coordMovs = null;
    juegoTerminado = false;
    newMov = false;
  }

  public String getTablero(){
    return tablero;
  }

  public void setTablero(String tablero) {
    this.tablero = tablero;
  }

  public ArrayList<String> getCoordenadas() {
    return coordenadas;
  }

  public void setCoordenadas(ArrayList<String> s) {
    coordenadas = s;
  }

  public String getCoord(){
    return coordMovs;
  }

  public void setCoord(String s) {
    coordMovs = s;
  }

  public void enviaTablero() {
    try {
      dos.writeUTF(tablero);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setTime(int time){
    this.time = time;
  }

  public int getTime() {
    return  time;
  }

  public void sendTime() {
    try {
      dos.writeInt(time);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void addPoint() {
    puntaje++;
  }

  public int getPoints(){
    return puntaje;
  }

  public void sendPoints() {
    try {
      dos.writeInt(puntaje);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void setContinue(boolean c) {
    cont = c;
  }

  public boolean getContinue() {
    return cont;
  }

  public boolean getJTerminado() {
    return juegoTerminado;
  }

  public void setJTerminado(boolean j) {
    juegoTerminado = j;
  }

  public void sendAll() {
    try{
      dos.writeInt(puntaje);
      dos.writeInt(time);
      dos.writeUTF("winner");
    }catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void waitResult() {
    while(!getContinue())
      ;
    if (getContinue()) {
      System.out.println("Puntaje: " + getPoints());
      System.out.println("Tiempo: " + getTime());
      sendPoints();
      System.out.println("Puntos enviados...");
      sendTime();
      System.out.println("Tiempo enviado: " + getTime());
      sendCoordenadas();
      setContinue(false);
    }
  }

  public void sendCoordenadas() {
    try {
      dos.writeInt(coordenadas.size());
      for (String coor : coordenadas) {
        dos.writeUTF(coor);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void readMoves() {
    try {
      coordMovs = dis.readUTF();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
