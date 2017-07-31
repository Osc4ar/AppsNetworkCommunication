import java.io.Serializable;
import java.rmi.server.UnicastRemoteObject;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.rmi.RemoteException;

public class ExchangeFiles extends UnicastRemoteObject implements InterfaceFiles {

  private byte[] allData;

  public ExchangeFiles(String fName) throws RemoteException {
    try {
      Path path = Paths.get("./Data/" + fName);
      allData = Files.readAllBytes(path);
      System.out.println("allData length: " + allData.length);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public byte[] getFile(int begin, int end) throws RemoteException {
    return allData;
  }

  public int getSize() throws RemoteException {
    return allData.length;
  }

}
