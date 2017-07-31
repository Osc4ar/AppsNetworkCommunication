import java.util.*;
import java.io.*;
import java.lang.reflect.*;

public class SGBD {
 
  public HashMap<String, HashMap<String, LinkedList<Object>>> all;
  private Hashtable<String, HashMap<String, LinkedList<Object>>> hashT;
  private ArrayList<String> tableF;
  private Class cT;
  private Field[] fields;

  public SGBD() {
    try {
      File source = new File("data");
      FileInputStream fis = new FileInputStream(source);
      ObjectInputStream ois = new ObjectInputStream(fis);
      all = (HashMap<String, HashMap<String, LinkedList<Object>>>)ois.readObject();
      ois.close();
      fis.close();
    } catch (Exception e) {
      hashT = new Hashtable<String, HashMap<String, LinkedList<Object>>>();
      all = new HashMap(hashT);
      e.printStackTrace();
    }
  }

  public void createDataBase(String db) {
    Hashtable<String, LinkedList<Object>> s = new Hashtable<String, LinkedList<Object>>();
    HashMap<String, LinkedList<Object>> map = new HashMap(s);
    all.put(db, map);
  }

  public void dropDB(String db) {
    all.remove(db);
  }

  public String showDB() {
    String dbs = "";
    Set set = all.entrySet();
    Iterator iterator = set.iterator();
    while(iterator.hasNext()) {
       Map.Entry mentry = (Map.Entry)iterator.next();
       dbs += mentry.getKey() + "&";
    }
    return dbs;
  }

  public void createTable(String db, String name, String definition) {
    tableF = new ArrayList<String>(Arrays.asList(definition.split(",")));
    try {
      Compiler.compile(name, tableF);
      cT = Class.forName(name);
      LinkedList<Object> nT = new LinkedList<Object>();
      HashMap<String, LinkedList<Object>> mDB = all.get(db);
      mDB.put(name, nT);
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public String showTables(String db) {
    String tables = "";
    HashMap<String, LinkedList<Object>> dbTables = all.get(db);
    Set set = dbTables.entrySet();
    Iterator iterator = set.iterator();
    while(iterator.hasNext()) {
       Map.Entry mentry = (Map.Entry)iterator.next();
       tables += mentry.getKey() + "&";
    }
    return tables;
  }

  public void dropTable(String db, String table) {
    HashMap<String, LinkedList<Object>> dbTables = all.get(db);
    dbTables.remove(table);
  }

  public String getTableDesc(String table) {
    try {
      String desc = table + "&Campo   Tipo&";
      cT = Class.forName(table);
      fields = cT.getDeclaredFields();
      int i = 1;
      for (Field f: fields)
        desc += f.getName() + " " + f.getType().getName() + "&";
      return desc;
    } catch(ClassNotFoundException ce) {
      ce.printStackTrace();
      return "Tabla no encontrada";
    }
  }

  public String insert(String db, String table, String values) {
    try {
      HashMap<String, LinkedList<Object>> dbTables = all.get(db);
      LinkedList<Object> listT = dbTables.get(table);
      String[] valuesA = values.split(",");
      cT = Class.forName(table);
      Object no = cT.newInstance();
      fields = cT.getDeclaredFields();
      int i = 0;
      for (Field f: fields) {
        String type = f.getType().getName();
        if (type.equalsIgnoreCase("int"))
          f.setInt(no, Integer.parseInt(valuesA[i]));
        else if (type.equalsIgnoreCase("double"))
          f.setDouble(no, Double.parseDouble(valuesA[i]));
        else if (type.equalsIgnoreCase("float"))
          f.setFloat(no, Float.parseFloat(valuesA[i]));
        else if (type.equalsIgnoreCase("boolean"))
          f.setBoolean(no, Boolean.parseBoolean(valuesA[i]));
        else
          f.set(no, valuesA[i]);
        i++;
      }
      listT.add(no);
      return "Registro insertado";
    } catch (Exception e) {
      e.printStackTrace();
      return "Tabla no encontrada";
    }
  }

  public void saveData() {
    try {
      File saving = new File("data");
      FileOutputStream fos = new FileOutputStream(saving);
      ObjectOutputStream oos = new ObjectOutputStream(fos);
      oos.writeObject(all);
      oos.flush();
      oos.close();
      fos.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public String insert(String db, String table, String params, String values) {
    try {
      HashMap<String, LinkedList<Object>> dbTables = all.get(db);
      LinkedList<Object> listT = dbTables.get(table);
      String[] valuesA = values.split(",");
      String[] paramsA = params.split(",");
      cT = Class.forName(table);
      Object no = cT.newInstance();
      fields = cT.getDeclaredFields();
      int i = 0;
      for (String param: paramsA) {
        param = param.replace(" ", "");
        Field field = null;
        for (Field f: fields) {
          if (param.equalsIgnoreCase(f.getName()))
            field = f;
        }
        String type = field.getType().getName();
        if (type.equalsIgnoreCase("int"))
          field.setInt(no, Integer.parseInt(valuesA[i]));
        else if (type.equalsIgnoreCase("double"))
          field.setDouble(no, Double.parseDouble(valuesA[i]));
        else if (type.equalsIgnoreCase("float"))
          field.setFloat(no, Float.parseFloat(valuesA[i]));
        else if (type.equalsIgnoreCase("boolean"))
          field.setBoolean(no, Boolean.parseBoolean(valuesA[i]));
        else
          field.set(no, valuesA[i]);
        i++;
      }
      listT.add(no);
      return "Registro insertado";
    } catch (Exception e) {
      e.printStackTrace();
      return "Tabla no encontrada";
    }
  }

  public String select(String db, String table) {
    try {
      String result = "";
      HashMap<String, LinkedList<Object>> dbTables = all.get(db);
      LinkedList<Object> listT = dbTables.get(table);
      Class cT = Class.forName(table);
      Field[] fields = cT.getDeclaredFields();
      for (Field f: fields)
        result += f.getName() + " ";
      result += "&";
      for (Object o: listT) {
        int i = 1;
        for (Field f: fields) {
          String type = f.getType().getName();
          String value = "";
          if (type.equalsIgnoreCase("int"))
            value = String.valueOf(f.getInt(o));
          else if (type.equalsIgnoreCase("double"))
            value = String.valueOf(f.getDouble(o));
          else if (type.equalsIgnoreCase("float"))
            value = String.valueOf(f.getFloat(o));
          else if (type.equalsIgnoreCase("boolean"))
            value = String.valueOf(f.getBoolean(o));
          else
            value = (String)f.get(o);
          result += value + " ";
        }
        result += "&";
      }
      return result;
    } catch(Exception e) {
      e.printStackTrace();
      return "Tabla no encontrada";
    }
  }

  public String select(String db, String table, String[] sentences) {
    try {
      String result = "";
      HashMap<String, LinkedList<Object>> dbTables = all.get(db);
      LinkedList<Object> listT = dbTables.get(table);
      Class cT = Class.forName(table);
      Field[] fields = cT.getDeclaredFields();
      Hashtable<String, String> sourceFields = new Hashtable<String, String>();
      HashMap<String, String> fieldsTC = new HashMap(sourceFields);
      for (Field f: fields)
        result += f.getName() + " ";
      result += "&";
      for (Object o: listT) {
        String temp = "";
        for (Field f: fields) {
          if (fieldsTC.size() != sentences.length) {
            for (String sentece: sentences) {
              String[] sSplited = sentece.split("=");
              if (sSplited[0].equalsIgnoreCase(f.getName()))
                fieldsTC.put(sSplited[0], sSplited[1]);
            }
          }
          String type = f.getType().getName();
          String value = "";
          if (type.equalsIgnoreCase("int"))
            value = String.valueOf(f.getInt(o));
          else if (type.equalsIgnoreCase("double"))
            value = String.valueOf(f.getDouble(o));
          else if (type.equalsIgnoreCase("float"))
            value = String.valueOf(f.getFloat(o));
          else if (type.equalsIgnoreCase("boolean"))
            value = String.valueOf(f.getBoolean(o));
          else
            value = (String)f.get(o);
          if (!temp.equalsIgnoreCase("nel"))
            temp += value + " ";
          if (fieldsTC.containsKey(f.getName())) {
            if (!value.equalsIgnoreCase(fieldsTC.get(f.getName())))
              temp = "nel";
          }
        }
        if (!temp.equalsIgnoreCase("nel"))
          result += temp + "&";
      }
      return result;
    } catch(Exception e) {
      e.printStackTrace();
      return "Tabla no encontrada";
    }
  }

  public String delete(String db, String table) {
      HashMap<String, LinkedList<Object>> dbTables = all.get(db);
      LinkedList<Object> listT = dbTables.get(table);
      listT.clear();
      return "Tabla " + table + "limpiada";
  }

  public String delete(String db, String table, String[] sentences) {
    try {
      HashMap<String, LinkedList<Object>> dbTables = all.get(db);
      LinkedList<Object> listT = dbTables.get(table);
      LinkedList<Object> toDel = new LinkedList<Object>();
      Class cT = Class.forName(table);
      Field[] fields = cT.getDeclaredFields();
      Hashtable<String, String> sourceFields = new Hashtable<String, String>();
      HashMap<String, String> fieldsTC = new HashMap(sourceFields);
      for (Object o: listT) {
        boolean del = false;
        for (Field f: fields) {
          if (fieldsTC.size() != sentences.length) {
            for (String sentece: sentences) {
              String[] sSplited = sentece.split("=");
              if (sSplited[0].equalsIgnoreCase(f.getName()))
                fieldsTC.put(sSplited[0], sSplited[1]);
            }
          }
          String type = f.getType().getName();
          String value = "";
          if (type.equalsIgnoreCase("int"))
            value = String.valueOf(f.getInt(o));
          else if (type.equalsIgnoreCase("double"))
            value = String.valueOf(f.getDouble(o));
          else if (type.equalsIgnoreCase("float"))
            value = String.valueOf(f.getFloat(o));
          else if (type.equalsIgnoreCase("boolean"))
            value = String.valueOf(f.getBoolean(o));
          else
            value = (String)f.get(o);
          if (fieldsTC.containsKey(f.getName()) && !del)
            del = value.equalsIgnoreCase(fieldsTC.get(f.getName()));
        }
        if (del)
          toDel.add(o);
      }
      for (Object tdel: toDel)
        listT.remove(tdel);
      return "Elementos eliminados";
    } catch(Exception e) {
      e.printStackTrace();
      return "Tabla no encontrada";
    }
  }

  public String update(String db, String table, String[] toSet, String[] sentences) {
    try {
      HashMap<String, LinkedList<Object>> dbTables = all.get(db);
      LinkedList<Object> listT = dbTables.get(table);
      Class cT = Class.forName(table);
      Field[] fields = cT.getDeclaredFields();
      Hashtable<String, String> sourceFields = new Hashtable<String, String>();
      HashMap<String, String> fieldsTC = new HashMap(sourceFields);
      HashMap<String, String> valuesTC = new HashMap(sourceFields);
      for (String exp: toSet) {
        String[] expSplited = exp.split("=");
        valuesTC.put(expSplited[0], expSplited[1]);
      }
      for (Object o: listT) {
        boolean change = false;
        for (Field f: fields) {
          if (fieldsTC.size() != sentences.length) {
            for (String sentece: sentences) {
              String[] sSplited = sentece.split("=");
              if (sSplited[0].equalsIgnoreCase(f.getName()))
                fieldsTC.put(sSplited[0], sSplited[1]);
            }
          }
          String type = f.getType().getName();
          String value = "";
          if (type.equalsIgnoreCase("int"))
            value = String.valueOf(f.getInt(o));
          else if (type.equalsIgnoreCase("double"))
            value = String.valueOf(f.getDouble(o));
          else if (type.equalsIgnoreCase("float"))
            value = String.valueOf(f.getFloat(o));
          else if (type.equalsIgnoreCase("boolean"))
            value = String.valueOf(f.getBoolean(o));
          else
            value = (String)f.get(o);
          if (fieldsTC.containsKey(f.getName()) && !change)
            change = value.equalsIgnoreCase(fieldsTC.get(f.getName()));
          if (change && valuesTC.containsKey(f.getName())) {
            if (type.equalsIgnoreCase("int"))
              f.setInt(o, Integer.parseInt(valuesTC.get(f.getName())));
            else if (type.equalsIgnoreCase("double"))
              f.setDouble(o, Double.parseDouble(valuesTC.get(f.getName())));
            else if (type.equalsIgnoreCase("float"))
              f.setFloat(o, Float.parseFloat(valuesTC.get(f.getName())));
            else if (type.equalsIgnoreCase("boolean"))
              f.setBoolean(o, Boolean.parseBoolean(valuesTC.get(f.getName())));
            else
              f.set(o, valuesTC.get(f.getName()));
          }
        }
      }
      return "Datos de la tabla " + table + " actualizados";
    } catch(Exception e) {
      e.printStackTrace();
      return "Tabla no encontrada";
    }
  }
}
