public class HiloVista extends Thread {
  Crucigrama game;

  public HiloVista() {
    game = new Crucigrama();
  }

  public void run() {
    System.out.println("Hilo independiente de interfaz...");
  }

  public static void main(String[] args) {
    HiloVista h = new HiloVista();
    h.start();
  }
}
