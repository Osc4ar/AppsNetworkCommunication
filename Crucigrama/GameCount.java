public class GameCount extends Thread {
  int time;
  boolean counting;

  public GameCount(int time) {
    this.time = time;
    counting = true;
  }

  public int getTime() {
    return time;
  }

  public boolean stillCounting() {
    return counting;
  }

  public void run() {
    try {
      while(time > 0) {
        Thread.sleep(1000);
        time--;
      }
      counting = false;
      System.out.println("Finalizo conteo...");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
