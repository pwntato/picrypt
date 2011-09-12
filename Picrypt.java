import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

class Picrypt extends JFrame implements ActionListener {

  private Container container = null;
  
  public Picrypt() {
	  super("Picrypt - Securely Embed Files in Pictures");
	  setDefaultCloseOperation(EXIT_ON_CLOSE);
	  
		container = getContentPane();
		container.setLayout(new GridBagLayout());
		
		setupMenu();
		
		CreateKey dlg = new CreateKey(this, container);
    dlg.setupDlg();
		
		this.setResizable(false);
		//this.setSize(510, 370);
		setVisible(true);
  }
  
  public void setupMenu() {
    JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
				
		fileMenu.add(setupMenu("Extract File From Image"));	
		fileMenu.add(setupMenu("Hide File In Image"));		
		fileMenu.addSeparator();
		fileMenu.add(setupMenu("Exit"));
		
		JMenu keyMenu = new JMenu("Manage Keys");
		menuBar.add(keyMenu);
		
		keyMenu.add(setupMenu("Create New Contact"));	
		keyMenu.add(setupMenu("Change Password"));	
		keyMenu.add(setupMenu("Import Contact Info"));	
		keyMenu.add(setupMenu("Export Contact Info"));	
		
		JMenu debugMenu = new JMenu("Debug");
		menuBar.add(debugMenu);
		debugMenu.add(setupMenu("Run Tests"));	
		
		setJMenuBar(menuBar);
  }

	public void actionPerformed(ActionEvent e) {
    if ("Run Tests".equals(e.getActionCommand())) {
      runTests();   // don't do this is the ui thread
    }
    else if ("Extract File From Image".equals(e.getActionCommand())) {
      ExtractImage dlg = new ExtractImage(this, container);
      dlg.setupDlg();
    }
    else if ("Hide File In Image".equals(e.getActionCommand())) {
      EmbedImage dlg = new EmbedImage(this, container);
      dlg.setupDlg();
    }
    else if ("Create New Contact".equals(e.getActionCommand())) {
      CreateKey dlg = new CreateKey(this, container);
      dlg.setupDlg();
    }
    else if ("Change Password".equals(e.getActionCommand())) {  
      ChangePassword dlg = new ChangePassword(this, container);
      dlg.setupDlg();
    }
    else if ("Import Contact Info".equals(e.getActionCommand())) {
      ImportKey dlg = new ImportKey(this, container);
      dlg.setupDlg();
    }
    else if ("Export Contact Info".equals(e.getActionCommand())) {
      ExportKey dlg = new ExportKey(this, container);
      dlg.setupDlg();
    }
    else if ("Exit".equals(e.getActionCommand())) {
      System.exit(0);
    }
    else {
      JOptionPane.showMessageDialog(this, "Unhandled action: " + e.getActionCommand());
    }
  } 
  
  public JMenuItem setupMenu(String menuText) {
		JMenuItem menuItem = new JMenuItem(menuText);
		menuItem.setActionCommand(menuText);
		menuItem.addActionListener(this);
		return menuItem;
  }
  
  public void runTests() {
    RSA rsa = new RSA();
    rsa.generateKeyPair();
    
    PicryptLib.saveKey("password".toCharArray(), rsa.getPubKey(), rsa.getPrivKey(), PicryptLib.KEY_STORE + "test.key");
    
    PicryptLib.getPubKey(PicryptLib.KEY_STORE + "test.key");
    
    PicryptLib.embedFile(PicryptLib.getPubKey(PicryptLib.KEY_STORE + "test.key"), "sample.doc", "sample.jpg", "output.png");     
    
    PrivateKey privKey = PicryptLib.getPrivKey("password".toCharArray(), PicryptLib.KEY_STORE + "test.key");
    String suggestedFileName = PicryptLib.getSuggestedFileName(privKey, "output.png");
    PicryptLib.extractFile(privKey, "output.png", suggestedFileName);
  }

  public static void main(String args[]) {
    Picrypt picrypt = new Picrypt();
  }
}

