import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceFiles extends Remote {
  public byte[] getFile(int begin, int end) throws RemoteException;
  public int getSize() throws RemoteException;
}

/*
java -cp "C:\Users\Oscar I. Castillo\Dropbox\Documentos\4toSemestre\AppsEnRed\3erParcial\Practicas\Practica8P2P";"C:\Users\Oscar I. Castillo\Dropbox\Documentos\4toSemestre\AppsEnRed\3erParcial\Practicas\Practica8P2P\Lemonwire.java"
     -Djava.rmi.server.codebase=file:"C:/Users/Oscar I. Castillo/Dropbox/Documentos/4toSemestre/AppsEnRed/3erParcial/Practicas/Practica8P2P/Lemonwire.java"
     -Djava.security.policy=no.policy
*/
