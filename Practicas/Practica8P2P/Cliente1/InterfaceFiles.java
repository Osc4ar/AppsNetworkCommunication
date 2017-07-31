import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceFiles extends Remote {
  public byte[] getFile(int begin, int end) throws RemoteException;
  public int getSize() throws RemoteException;
}
