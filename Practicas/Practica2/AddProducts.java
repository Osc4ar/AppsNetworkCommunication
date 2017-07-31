import javax.swing.*;
import java.net.*;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;

public class AddProducts extends JFrame implements ActionListener {

  private JLabel nameJL, descriptionJL, costJL, stockJL, imageJL, offerJL, brandJL, modelJL, pictureJL;
  private JButton addProduct, deleteProduct;

  public AddProducts() {
    super("Add Products");
    setLayout(new FlowLayout());
    setSize(500, 500);
    JLabel nameJL = new JLabel("Name: Default");
    add(nameJL);
    JLabel descriptionJL = new JLabel("Description: null");
    add(descriptionJL);
    JLabel costJL = new JLabel("Cost: $0.00");
    add(costJL);
    JLabel stockJL = new JLabel("Stock: 0");
    add(stockJL);
    JLabel imageJL = new JLabel("Images: ");
    add(imageJL);
    JLabel pictureJL = new JLabel("Picture");
    add(pictureJL);
    JLabel offerJL = new JLabel("Offers: 0%");
    add(offerJL);
    JLabel brandJL = new JLabel("Brand: null");
    add(brandJL);
    JLabel modelJL = new JLabel("Model: AAA10");
    add(modelJL);
  }

  public static void main(String[] args) {
    new AddProducts();
  }
}
