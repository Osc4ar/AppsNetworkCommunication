import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Timer;
import java.util.TimerTask;

public class Server {

  private int clientes = 0;
  private Socket[] socketsClientes;
  private ServerSocket ss;
  private HilosClientes[] threadClientes;
  private Timer CountTime;
  private String tablero;
  public ArrayList<String> coordenadas;
  public ArrayList<String> unableCoordenadas;

  public Server() {
    try {
      coordenadas = new ArrayList<String>();
      unableCoordenadas = new ArrayList<String>();
      threadClientes = new HilosClientes[3];
      socketsClientes = new Socket[3];
      ss = new ServerSocket(5000);
      for (int i = 0; i < 3; i++) {
        socketsClientes[i] = ss.accept();
        System.out.println("Cliente: " + i);
        if (i == 0) {
          threadClientes[i] = new HiloPrincipal(socketsClientes[i], "");
          threadClientes[i].setName("Principal");
          threadClientes[i].start();
        }
        else {
          tablero = threadClientes[0].getTablero();
          threadClientes[i] = new HiloSecundario(socketsClientes[i], tablero);
          threadClientes[i].setName("Secundario" + i);
          threadClientes[i].start();
        }
      }
      coordenadas = threadClientes[0].getCoordenadas();
      for (String coordenada : coordenadas) {
        System.out.println(coordenada);
      }
      GameCount g = new GameCount(300);
      g.start();
      String coords = null;
      while(g.stillCounting()) {
        for (int i = 0; i < 3; i++) {
          threadClientes[i].setTime(g.getTime());
          coords = threadClientes[i].getCoord();
          threadClientes[i].setCoordenadas(unableCoordenadas);
          if (coords != null) {
            System.out.println("Validando coordenadas: " + coords);
            if (coordenadas.remove(coords)) {
              System.out.println("Coordenadas correctas");
              threadClientes[i].addPoint();
              unableCoordenadas.add(coords);
            } else {
              System.out.println("Coordenadas incorrectas");
            }
            threadClientes[i].setContinue(true);
            threadClientes[i].setCoord(null);
          }
        }
      }
      for (int i = 0; i < 3; i++) {
        threadClientes[i].setJTerminado(true);
      }
      System.exit(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
      System.out.println("Server termino");
      for (HilosClientes player : threadClientes) {
        player.juegoTerminado = true;
      }
    }

  public boolean validarCoord(String coorde){
    if (!coordenadas.remove(coorde))
      return true;
    return false;
  }
  public static void main(String[] args) {
    System.out.println("Inicializando servidor... ");
    new Server();
  }
}
