import java.nio.channels.*;
import java.nio.*;
import java.io.*;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Iterator;

class Game {
    SocketChannel player1, player2;
    Move move;
    int noJugador;
    public Game() {
        player1 = null;
        player2 = null;
        move = null;
    }
}

public class Server {
    public static void main(String args[]) {
        try{
           Move tosend = null;
           ServerSocketChannel s = ServerSocketChannel.open();
           s.configureBlocking(false);
           s.socket().bind(new InetSocketAddress(9999));
           Selector sel = Selector.open();
           s.register(sel, SelectionKey.OP_ACCEPT);
           System.out.println("Servidor listo.. esperando clientes..."); 
           /*
            
           
           */
           ArrayList<Game> games = new ArrayList<Game>();
           games.add(new Game());
           int pl = 0;
           int player = 0;
           while(true){
               System.out.println("Ciclo infinito");
               sel.select();
               Iterator<SelectionKey> it = sel.selectedKeys().iterator();
               while(it.hasNext()){
                   System.out.println("Server esperando");
                   SelectionKey k = (SelectionKey)it.next();
                   it.remove();
                   if(k.isAcceptable()){
                       ServerSocketChannel sch = (ServerSocketChannel)k.channel();
                       SocketChannel cl = sch.accept();
                       System.out.println("Cliente conectado desde: "+cl.socket().getInetAddress()+":"+cl.socket().getPort());
                       if(pl == 0) {
                           games.get(games.size() - 1).player1 = cl;
                           pl++;
                           player = 1;
                           System.out.println("Player: " + player);
                       }
                       if(pl == 1) {
                           if(games.get(games.size() - 1).player1.socket().getPort() != cl.socket().getPort()) {
                                games.get(games.size() - 1).player2 = cl;
                                pl = 0;
                                player = 2;
                                System.out.println("Player: " + player);
                                games.add(new Game());
                            }
                       }
                       cl.configureBlocking(false);
                       cl.register(sel, SelectionKey.OP_READ|SelectionKey.OP_WRITE);
                       continue;
                   }//if
                   if(k.isReadable()){
                       System.out.println("Algo leible");
                       ByteBuffer b = ByteBuffer.allocate(2000);
                       b.clear();
                       SocketChannel ch = (SocketChannel)k.channel();
                       Game g = null;
                       player = 0;
                       for(Game temp:games) {
                           if(temp.player1 == ch) {
                               g = temp;
                               player = 1;
                           }
                           if(temp.player2 == ch) {
                               g = temp;
                               player = 2;
                           }
                       }
                       ch.read(b);
                       b.flip();
                       if(b.hasArray()){
                           ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(b.array()));
                           g.move = (Move)ois.readObject();
                           tosend = g.move;
                           System.out.println("Objeto recibido..");
                           System.out.println("Valor i: " + tosend.i + "\nValor j: " + tosend.j);
                           if(player == 1) {
                               g.player2.write(b);
                               System.out.println("Enviado a jugador 2");
                           }
                           else {
                               g.player1.write(b);
                               System.out.println("Enviado a jugador 1");
                           }
                           //k.cancel();
                       }//if
                       continue;
                   }
                   else if (k.isWritable()){
                       SocketChannel ch2 = (SocketChannel)k.channel();
                       ByteBuffer buff = ByteBuffer.allocate(2000);
                       buff.asIntBuffer().put(player);
                       System.out.println("Enviando numero de jugador: " + player);
                       ch2.write(buff);
                       k.interestOps(SelectionKey.OP_READ);
                       continue;
                   }//else
                   /*
                   else if (k.isWritable()){
                       ByteArrayOutputStream baos = new ByteArrayOutputStream();
                       ObjectOutputStream oos = new ObjectOutputStream(baos);
                       oos.writeObject(tosend);
                       oos.flush();
                       ByteBuffer b = ByteBuffer.wrap(baos.toByteArray());
                       SocketChannel cl = (SocketChannel)k.channel();
                       cl.write(b);
                       tosend = null;
                   }//else*/
               }//while
           }//while

       }catch(Exception e){
           e.printStackTrace();
       }//catch
    }
}
