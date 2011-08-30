import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

class Picrypt extends JFrame implements ActionListener {	
  private JLabel output = null;
    
  public Picrypt() {
	  super("Picrypt - Securely Embed Files in Pictures");
	  setDefaultCloseOperation(EXIT_ON_CLOSE);
	  
		Container container = getContentPane();
		container.setLayout(new FlowLayout());
		
		JMenuBar menuBar = new JMenuBar();
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
				
		fileMenu.add(setupMenu("Import Image"));	
		fileMenu.add(setupMenu("Embed Image"));		
		fileMenu.addSeparator();
		fileMenu.add(setupMenu("Exit"));
		
		setJMenuBar(menuBar);
		
		Container importImage = new Container();
		importImage.setLayout(new FlowLayout());
		JLabel importLabel = new JLabel("Import an Image");
		importImage.add(importLabel);
		container.add(importImage);
		
		JTextField input = new JTextField(30);
		JButton run_tests = new JButton("Run Tests");
		run_tests.setActionCommand("run_tests");
		run_tests.addActionListener(this);
		output = new JLabel("Status");
		
		container.add(input);
		container.add(run_tests);
		container.add(output);
		
		this.setSize(800, 800);
		setVisible(true);
  }
	
	public void actionPerformed(ActionEvent e) {
    if ("run_tests".equals(e.getActionCommand())) {
      output.setText("Running Tests");
      runTests();   // don't do this is the ui thread
      output.setText("Tests Completed");
    }
    else if ("import_image".equals(e.getActionCommand())) {
      JOptionPane.showMessageDialog(this, "Import Image");
    }
    else if ("embed_image".equals(e.getActionCommand())) {
      JOptionPane.showMessageDialog(this, "Embed Image");
    }
    else if ("exit".equals(e.getActionCommand())) {
      System.exit(0);
    }
  } 
  
  public JMenuItem setupMenu(String menuText) {
		JMenuItem menuItem = new JMenuItem(menuText);
		menuItem.setActionCommand(menuText.replace(" ", "_").toLowerCase());
		menuItem.addActionListener(this);
		return menuItem;
  }
  
  public void runTests() {
    RSA rsa = new RSA();
    rsa.generateKeyPair();
    
    PicryptLib.embedPubKey(rsa.getPubKey(), "sample.jpg", "output.pubKey.png");
    PicryptLib.embedKey("password", rsa.getPubKey(), rsa.getPrivKey(), "sample.jpg", "output.key.png");
    
    PicryptLib.embedFile(PicryptLib.extractPubKey("output.pubKey.png"), "~/java/picrypt/sample.doc", "sample.jpg", "output.png");     
    
    PrivateKey privKey = PicryptLib.extractPrivKey("password", "output.key.png");
    String suggestedFileName = PicryptLib.getSuggestedFileName(privKey, "output.png");
    PicryptLib.extractFile(privKey, "output.png", suggestedFileName);
  }

  public static void main(String args[]) {
    Picrypt picrypt = new Picrypt();
  }
}

