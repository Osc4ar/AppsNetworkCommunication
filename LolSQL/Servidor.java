import java.io.*;
import java.net.*;
import java.lang.String;

public class Servidor {

  private static SGBD sgbd;
  private static String dbselected = "null";

  public static String sentencia(String sentencia) {
    if (sentencia.startsWith("create database ")) {
      String db = sentencia.substring(16, sentencia.indexOf(';'));
      sgbd.createDataBase(db);
      return "Base de datos " + db + " creada";
    }
    if (sentencia.startsWith("show databases;")) {
      return sgbd.showDB();
    }
    if (sentencia.startsWith("use ")) {
      dbselected = sentencia.substring(4, sentencia.indexOf(';'));
      return "Database changed to " + dbselected;
    }
    if (sentencia.startsWith("drop database ")) {
      String db = sentencia.substring(14, sentencia.indexOf(';'));
      sgbd.dropDB(db);
      return "Base de datos " + db + " eliminada";
    }
    if (sentencia.startsWith("create table ")) {
      if (!dbselected.equalsIgnoreCase("null")) {
        String tabla = sentencia.substring(13, sentencia.indexOf('('));
        String definicion = sentencia.substring(sentencia.indexOf('(') + 1, sentencia.indexOf(')'));
        sgbd.createTable(dbselected, tabla, definicion);
        return "Tabla " + tabla + " creada";
      }
      return "Selecciona una base de datos";
    }
    if (sentencia.startsWith("show tables;")) {
      if (!dbselected.equalsIgnoreCase("null")) {
        return sgbd.showTables(dbselected);
      }
      return "Selecciona una base de datos";
    }
    if (sentencia.startsWith("desc ") || sentencia.startsWith("describe ")) {
      if (!dbselected.equalsIgnoreCase("null")) {
        String table = "";
        if (sentencia.startsWith("desc "))
          table = sentencia.substring(5, sentencia.indexOf(";"));
        else
          table = sentencia.substring(9, sentencia.indexOf(";"));
        return sgbd.getTableDesc(table);
      }
      return "Selecciona una base de datos";
    }
    if (sentencia.startsWith("drop table ")) {
      if (!dbselected.equalsIgnoreCase("null")) {
        String table = sentencia.substring(11, sentencia.indexOf(";"));
        sgbd.dropTable(dbselected, table);
        return "Tabla " + table + " eliminada";
      }
      return "Selecciona una base de datos";
    }
    if (sentencia.startsWith("insert into ")) {
      if (!dbselected.equalsIgnoreCase("null")) {
        String table = "";
        String values = "";
        String result = "";
        if (sentencia.indexOf("(", sentencia.indexOf("(") + 1) == -1) {
          table = sentencia.substring(12, sentencia.indexOf("values")).replace(" ", "");
          values = sentencia.substring(sentencia.indexOf("(") + 1, sentencia.indexOf(")"));
          result = sgbd.insert(dbselected, table, values);
        } else {
          table = sentencia.substring(12, sentencia.indexOf("(")).replace(" ", "");
          String params = sentencia.substring(sentencia.indexOf("(") + 1, sentencia.indexOf(")")).replace(" ", "");
          values = sentencia.substring(sentencia.indexOf("(", sentencia.indexOf("(") + 1) + 1, sentencia.indexOf(")", sentencia.indexOf(")") + 1));
          result = sgbd.insert(dbselected, table, params, values);
        }
        return result;
      }
      return "Selecciona una base de datos";
    }
    if (sentencia.startsWith("select * from ")) {
      if (!dbselected.equalsIgnoreCase("null")) {
        String table = "";
        int indexW = sentencia.indexOf("where");
        if (indexW != -1) {
          String where = sentencia.substring(sentencia.indexOf(" ", indexW), sentencia.indexOf(";"));
          table = sentencia.substring(14, indexW - 1);
          where  = where.replace(" ", "");
          String[] sentences = where.split("&&");
          return sgbd.select(dbselected, table, sentences);
        }
        table = sentencia.substring(14, sentencia.indexOf(";"));
        return sgbd.select(dbselected, table);
      }
      return "Selecciona una base de datos";
    }
    if (sentencia.startsWith("delete * from ")) {
      if (!dbselected.equalsIgnoreCase("null")) {
        String table = "";
        int indexW = sentencia.indexOf("where");
        if (indexW != -1) {
          String where = sentencia.substring(sentencia.indexOf(" ", indexW), sentencia.indexOf(";"));
          table = sentencia.substring(14, indexW - 1);
          where  = where.replace(" ", "");
          String[] sentences = where.split("&&");
          return sgbd.delete(dbselected, table, sentences);
        }
        table = sentencia.substring(14, sentencia.indexOf(";"));
        return sgbd.delete(dbselected, table);
      }
      return "Selecciona una base de datos";
    }
    if (sentencia.startsWith("update ")) {
      if (!dbselected.equalsIgnoreCase("null")) {
        int indexW = sentencia.indexOf("where");
        int indexS = sentencia.indexOf("set");
        String table = sentencia.substring(7, sentencia.indexOf(" ", 7));
        String set = sentencia.substring(indexS + 4, indexW);
        set = set.replace(" ", "");
        String where = sentencia.substring(indexW + 6, sentencia.indexOf(";"));
        where  = where.replace(" ", "");
        String[] toSet = set.split(",");
        String[] sentences = where.split("&&");
        return sgbd.update(dbselected, table, toSet, sentences);
      }
      return "Selecciona una base de datos";
    }
    if (sentencia.startsWith("exit;")) {
      return "bye";
    }
    return "nel";
  }

  public static void main(String[] args) {
    try {
      int pto = 5555;
      sgbd = new SGBD();
      ServerSocket s = new ServerSocket(pto);
      s.setReuseAddress(true);
      System.out.println("LolSQL iniciado... Esperando clientes");
      for(; ; ) {
        Socket cl = s.accept();
        System.out.println("Cliente conectado desde: " + cl.getInetAddress() + ":" + cl.getPort());
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
        BufferedReader br = new BufferedReader(new InputStreamReader(cl.getInputStream()));
        String sentencia = "";
        for (; ; ) {
          sentencia = br.readLine();
          if (sentencia.compareToIgnoreCase("exit;") == 0) {
            System.out.println("El cliente termino");
            br.close();
            pw.close();
            cl.close();
            dbselected = "null";
            break;
          } else {
            System.out.println("Sentencia recibida: " + sentencia);
            pw.println(sentencia(sentencia));
            pw.flush();
            sgbd.saveData();
            continue;
          }
        }
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}
