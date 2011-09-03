import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

class Picrypt extends JFrame implements ActionListener {	
  private JTextField name = null;
  private JPasswordField newPassword1 = null;
  private JPasswordField newPassword2 = null;
  private JTextArea pubKey = null;

  public Picrypt() {
	  super("Picrypt - Securely Embed Files in Pictures");
	  setDefaultCloseOperation(EXIT_ON_CLOSE);
	  
		Container container = getContentPane();
		container.setLayout(new GridBagLayout());
		GridBagConstraints gridProps = null;
		
		setupMenu();
				
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 0;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Enter name:"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 0;
		name = new JTextField(30);
		container.add(name, gridProps);
				
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 1;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Enter password:"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 1;
		newPassword1 = new JPasswordField(30);
		container.add(newPassword1, gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 2;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Enter password again:"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 2;
		newPassword2 = new JPasswordField(30);
		container.add(newPassword2, gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 3;
		gridProps.gridwidth = 2;
		container.add(setupButton("Create Contact Info"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 4;
		gridProps.gridwidth = 2;
		container.add(new JLabel("<html><div align='center'><strong>Picrypt contact information</strong><br/><br/>(Allows people to send you files hidden in images)</div></html>"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 5;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.BOTH;
		pubKey = new JTextArea();
		pubKey.setRows(5);
		pubKey.setLineWrap(true);
    pubKey.setWrapStyleWord(false);
		container.add(pubKey, gridProps);
		
		this.setSize(800, 800);
		setVisible(true);
  }
  
  public void setupMenu() {
    JMenuBar menuBar = new JMenuBar();
		
		JMenu fileMenu = new JMenu("File");
		menuBar.add(fileMenu);
				
		fileMenu.add(setupMenu("Extract File From Image"));	
		fileMenu.add(setupMenu("Embed File In Image"));		
		fileMenu.addSeparator();
		fileMenu.add(setupMenu("Exit"));
		
		JMenu keyMenu = new JMenu("Manage Keys");
		menuBar.add(keyMenu);
		
		keyMenu.add(setupMenu("Create New Contact"));	
		keyMenu.add(setupMenu("Change Password"));	
		keyMenu.add(setupMenu("Import Contact Info"));	
		keyMenu.add(setupMenu("Export Contact Info"));	
		
		setJMenuBar(menuBar);
  }
	
	public void actionPerformed(ActionEvent e) {
    if ("Run Tests".equals(e.getActionCommand())) {
      runTests();   // don't do this is the ui thread
    }
    else if ("Extract File From Image".equals(e.getActionCommand())) {
      JOptionPane.showMessageDialog(this, "Extract File From Image");
    }
    else if ("Embed File In Image".equals(e.getActionCommand())) {
      JOptionPane.showMessageDialog(this, "Embed File In Image");
    }
    else if ("Create New Contact".equals(e.getActionCommand())) {
      JOptionPane.showMessageDialog(this, "Create New Contact");
    }
    else if ("Change Password".equals(e.getActionCommand())) {
      JOptionPane.showMessageDialog(this, "Change Password");
    }
    else if ("Import Contact Info".equals(e.getActionCommand())) {
      JOptionPane.showMessageDialog(this, "Import Contact Info");
    }
    else if ("Export Contact Info".equals(e.getActionCommand())) {
      JOptionPane.showMessageDialog(this, "Export Contact Info");
    }
    else if ("Create Contact Info".equals(e.getActionCommand())) {
      String pwd1 = new String(newPassword1.getPassword());
      String pwd2 = new String(newPassword2.getPassword());
      if (pwd1.equals(pwd2)) {
        RSA rsa = new RSA();
        rsa.generateKeyPair();
        
        byte[] rawPub = PicryptLib.catArrays(rsa.getPubKey().getEncoded(), name.getText().getBytes());
        
        byte[] aesKey = AES.passwordToKey(pwd1);
        AES aes = new AES();
        aes.setKey(aesKey);
        byte[] rawPriv = aes.encrypt(rsa.getPrivKey().getEncoded());
        byte[] iv = aes.getIv(); 
        byte[] aesPriv = PicryptLib.catArrays(iv, rawPriv);
        
        pubKey.setText(Base64.encodeBytes(rawPub));
        
        byte[] toFile = PicryptLib.catArrays(aesPriv, rawPub);
        PicryptLib.saveFile(name.getText().replace(' ', '_').toLowerCase() + ".key", toFile);
      }
      else {
        JOptionPane.showMessageDialog(this, "Passwords don't match");
      }
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
  
  public JButton setupButton(String buttonText) {
		JButton button = new JButton(buttonText);
		button.setActionCommand(buttonText);
		button.addActionListener(this);
		return button;
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

