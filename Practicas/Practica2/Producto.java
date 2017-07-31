import java.io.Serializable;


public class Producto implements Serializable {

  int id;
  String descripcion;
  int precio;
  int existencias;
  String [] fotos;
  String marca;
  String modelo;

  public Producto(int id,String descripcion,int precio,int existencias,String [] fotos,String marca,String modelo){

    this.id = id;
    this.descripcion = descripcion;
    this.precio = precio;
    this.existencias = existencias;
    this.fotos = fotos;
    this.marca = marca;
    this.modelo = modelo;
  }
}
