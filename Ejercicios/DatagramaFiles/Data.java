import java.io.Serializable;

public class Data implements Serializable {
  long id;
  String name;
  long size;
  byte[] b;

  public Data(long id, String name, long size, long nb) {
    this.id = id;
    this.name = name;
    this.size = size;
    b = new byte[nb];
  }
}
