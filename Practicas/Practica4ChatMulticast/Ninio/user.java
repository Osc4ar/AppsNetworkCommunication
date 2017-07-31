import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class user extends JFrame implements ActionListener{
  JTextField jt;
  JButton b1;
  JPanel jp1;

  public user(){
    super("User");
    setLayout(new FlowLayout());
    GridLayout grid = new GridLayout(2,1);
    jt = new JTextField("Ingress a nickname");
    b1 = new JButton("SEND");
    jp1 = new JPanel();
    jp1.setLayout(grid);
    jp1.add(jt);
    jp1.add(b1);
    add(jp1);
    b1.addActionListener(this);
    setSize(200,100);
    setVisible(true);

  }
  public void actionPerformed (ActionEvent e){

    JButton b = (JButton)e.getSource();

    if (b==b1) {

      new cliente(jt.getText());
      setVisible(false);
    }
  }
  public static void main(String[] args) {
    new user();

  }

}
