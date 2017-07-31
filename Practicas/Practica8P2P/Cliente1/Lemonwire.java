import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

public class Lemonwire extends JFrame implements ActionListener, Runnable {

  private JButton jbDownload, jbUsername;
  private JTextField jtfUsername;
  private JList jListFiles;
  private DefaultListModel dModel;
  private ArrayList<String> arrListFiles;
  private Server server;
  private Client client;

  public Lemonwire() {
    setTitle("Lemonwire");
    setSize(400, 400);
    setLayout(new FlowLayout());
    setDefaultCloseOperation(EXIT_ON_CLOSE);
    jtfUsername = new JTextField(20);
    add(jtfUsername);
    jbUsername = new JButton("Start");
    jbUsername.addActionListener(this);
    add(jbUsername);
    jbDownload = new JButton("Download");
    jbDownload.addActionListener(this);
    add(jbDownload);
    arrListFiles = new ArrayList<String>();
    dModel = new DefaultListModel();
    jListFiles = new JList(dModel);
    jListFiles.setLayoutOrientation(JList.VERTICAL);
    JScrollPane listScroller = new JScrollPane(jListFiles);
    listScroller.setPreferredSize(new Dimension(350, 290));
    add(listScroller);
    setVisible(true);
  }

  public void run() {
    try {
      while(true) {
        Thread.sleep(1000);
        arrListFiles = server.getArrListFiles();
        updateJList();
      }
    } catch(Exception e) {
      e.printStackTrace();
    }
  }

  public void updateJList() {
    for (String s : arrListFiles) {
      if (!dModel.contains(s))
        dModel.addElement(s);
    }
  }

  public void actionPerformed(ActionEvent aEvent) {
    JButton clicked = (JButton) aEvent.getSource();
    if (clicked == jbDownload) {
      server.askFile((String)jListFiles.getSelectedValue());
    } else {
      server = new Server(jtfUsername.getText());
      server.start();
      new Thread(this).start();
    }
  }

  public static void main(String[] args) {
    new Lemonwire();
  }

}
