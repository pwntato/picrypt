import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

class Picrypt extends JFrame implements ActionListener {

  private Container container = null;

  private JTextField name = null;
  private JPasswordField newPassword1 = null;
  private JPasswordField newPassword2 = null;
  private JTextArea pubKey = null;
  
  private JComboBox keyNames = null;
  
  private JButton saveAsButton = null;
  
  private JTextField toHideName = null;
  private JTextField imgHideName = null;
  private JTextField imgSaveName = null;
  private JTextField fileSaveName = null;
  
  private File fileToHide = null;
  private File imgToHideIn = null;
  private File imgToSave = null;
  private File fileToSave = null;

  public Picrypt() {
	  super("Picrypt - Securely Embed Files in Pictures");
	  setDefaultCloseOperation(EXIT_ON_CLOSE);
	  
		container = getContentPane();
		container.setLayout(new GridBagLayout());
		
		setupMenu();
		
		setupExtractImageDlg();
		
		this.setResizable(false);
		this.setSize(510, 370);
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
  
  public void setupExtractImageDlg() {
    container.removeAll();
    container.repaint();
    this.setSize(510, 370);
  
    GridBagConstraints gridProps = null;
  
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 0;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Decrypt as:"), gridProps);

		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 0;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
    container.add(setupKeyNameDropDown(), gridProps);    
  
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 1;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Password:"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 1;
		newPassword1 = new JPasswordField(30);
		container.add(newPassword1, gridProps);
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 2;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(setupButton("Image to decrypt"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 2;
		gridProps.anchor = GridBagConstraints.LINE_START;
		imgHideName = new JTextField(30);
		imgHideName.setText("[No File]");
		imgHideName.setEditable(false);
		container.add(imgHideName, gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 3;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		gridProps.anchor = GridBagConstraints.LINE_END;
		saveAsButton = setupButton("Save as");
		container.add(saveAsButton, gridProps);
		saveAsButton.setEnabled(false);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 3;
		gridProps.anchor = GridBagConstraints.LINE_START;;
		fileSaveName = new JTextField(30);
		fileSaveName.setText("[No File]");
		fileSaveName.setEditable(false);
		container.add(fileSaveName, gridProps);
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 4;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		container.add(setupButton("Extract File"), gridProps);
		
		setVisible(true);
  }
  
  public void setupEmbedImageDlg() {
    container.removeAll();
    container.repaint();
    this.setSize(510, 370);
  
    GridBagConstraints gridProps = null;
  
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 0;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Encrypt to:"), gridProps);

		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 0;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
    container.add(setupKeyNameDropDown(), gridProps);
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 1;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(setupButton("File to hide"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 1;
		gridProps.anchor = GridBagConstraints.LINE_START;
		toHideName = new JTextField(30);
		toHideName.setText("[No File]");
		toHideName.setEditable(false);
		container.add(toHideName, gridProps);
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 3;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(setupButton("Image to hide in"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 3;
		gridProps.anchor = GridBagConstraints.LINE_START;
		imgHideName = new JTextField(30);
		imgHideName.setText("[No File]");
		imgHideName.setEditable(false);
		container.add(imgHideName, gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 4;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(setupButton("Save as"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 4;
		gridProps.anchor = GridBagConstraints.LINE_START;;
		imgSaveName = new JTextField(30);
		imgSaveName.setText("[No File]");
		imgSaveName.setEditable(false);
		container.add(imgSaveName, gridProps);
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 5;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		container.add(setupButton("Hide File"), gridProps);
		
		setVisible(true);
  }
  
  public JComboBox setupKeyNameDropDown() {
    String[] keys = (new File(PicryptLib.KEY_STORE)).list();
		for (int i=0; i<keys.length; i++) { 
		  keys[i] = keys[i].substring(0, keys[i].length() - 4).replace('_', ' ');
		}
		java.util.Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
		keyNames = new JComboBox(keys);
		
		return keyNames;
  }
  
  public void setupNewKeyDlg() {
    container.removeAll();
    container.repaint();
    this.setSize(510, 370);
  
    GridBagConstraints gridProps = null;
  
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
		container.add(new JLabel("Picrypt contact information"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 5;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.BOTH;
		gridProps.weighty = 1;
		pubKey = new JTextArea();
		pubKey.setLineWrap(true);
    pubKey.setWrapStyleWord(false);
    pubKey.setEditable(false);
    pubKey.setText("\n\n\n\n\n\n\n\n\n\n\n\n");
		container.add(pubKey, gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 6;
		gridProps.gridwidth = 2;
		container.add(new JLabel("Give your contact info to anyone you want to get Picrypt files from"), gridProps);
		
		setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
    if ("Run Tests".equals(e.getActionCommand())) {
      runTests();   // don't do this is the ui thread
    }
    else if ("Extract File From Image".equals(e.getActionCommand())) {
      setupExtractImageDlg();
    }
    else if ("Hide File In Image".equals(e.getActionCommand())) {
      setupEmbedImageDlg();
    }
    else if ("File to hide".equals(e.getActionCommand())) {
      JFileChooser fc = new JFileChooser();
      int returnVal = fc.showOpenDialog(this);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        fileToHide = fc.getSelectedFile();
        
        toHideName.setText(fileToHide.getName());
      }
    }
    else if ("Image to hide in".equals(e.getActionCommand())) {
      JFileChooser fc = new JFileChooser();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      fc.addChoosableFileFilter(new ImgFilter());
      int returnVal = fc.showOpenDialog(this);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        imgToHideIn = fc.getSelectedFile();
        imgHideName.setText(imgToHideIn.getName());
      }
    }
    else if ("Save as".equals(e.getActionCommand())) {
      JFileChooser fc = new JFileChooser();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      fc.addChoosableFileFilter(new ImgFilter());
      fc.setSelectedFile(new File("picrypt.png"));
      int returnVal = fc.showSaveDialog(this);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        imgToSave = fc.getSelectedFile();
        imgSaveName.setText(imgToSave.getName());
      }
    }
    else if ("Save file as".equals(e.getActionCommand())) {
      JFileChooser fc = new JFileChooser();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      fc.addChoosableFileFilter(new ImgFilter());
      fc.setSelectedFile(new File("picrypt.png"));
      int returnVal = fc.showSaveDialog(this);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        imgToSave = fc.getSelectedFile();
        imgSaveName.setText(imgToSave.getName());
      }
    }
    else if ("Hide File".equals(e.getActionCommand())) {
      if (fileToHide == null) {
        JOptionPane.showMessageDialog(this, "You must select a file to hide");
      }
      else if (imgToHideIn == null) {
        JOptionPane.showMessageDialog(this, "You must select an image to hide in");
      }
      else if (imgToSave == null) {
        JOptionPane.showMessageDialog(this, "You must select where to save the new image");
      }
      else {
        PublicKey publicKey = PicryptLib.getPubKey(PicryptLib.KEY_STORE + ((String)keyNames.getSelectedItem()).replace(' ', '_') + ".key");
        PicryptLib.embedFile(publicKey, fileToHide.getPath(), imgToHideIn.getPath(), imgToSave.getPath());
      }
    }
    else if ("Image to decrypt".equals(e.getActionCommand())) {
      JFileChooser fc = new JFileChooser();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      fc.addChoosableFileFilter(new ImgFilter());
      int returnVal = fc.showOpenDialog(this);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        imgToHideIn = fc.getSelectedFile();
        char[] password = newPassword1.getPassword();
        if (password.length > 0) {
          PrivateKey privateKey = PicryptLib.getPrivKey(password, PicryptLib.KEY_STORE + ((String)keyNames.getSelectedItem()).replace(' ', '_') + ".key");
          fileToSave = new File(PicryptLib.getSuggestedFileName(privateKey, imgToHideIn.getPath()));
          imgHideName.setText(imgToHideIn.getName());
          fileSaveName.setText(fileToSave.getName());
          saveAsButton.setEnabled(true);
          
          for (int i=0; i<password.length; i++) {
            password[i] = 0;
          }
        }
      }      
    }
    else if ("Extract File".equals(e.getActionCommand())) {
      char[] password = newPassword1.getPassword();
      if (password.length > 0) {
        PrivateKey privateKey = PicryptLib.getPrivKey(password, PicryptLib.KEY_STORE + ((String)keyNames.getSelectedItem()).replace(' ', '_') + ".key");
        PicryptLib.extractFile(privateKey, imgToHideIn.getPath(), fileToSave.getPath());
        
        for (int i=0; i<password.length; i++) {
          password[i] = 0;
        }
      } 
    }
    else if ("Create New Contact".equals(e.getActionCommand())) {
      setupNewKeyDlg();
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
      char[] pwd1 = newPassword1.getPassword();
      char[] pwd2 = newPassword2.getPassword();
      if (pwd1.equals(pwd2)) {
        createKey(name.getText(), pwd1);
              
        for (int i=0; i<pwd1.length; i++) {
          pwd1[i] = 0;
          pwd2[i] = 0;
        }
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
  
  public void createKey(String name, char[] password) {
    RSA rsa = new RSA();
    rsa.generateKeyPair();
    
    byte[] rawPub = PicryptLib.catArrays(rsa.getPubKey().getEncoded(), name.getBytes());
    
    pubKey.setText(Base64.encodeBytes(rawPub));
    
    PicryptLib.saveKey(password, rsa.getPubKey(), rsa.getPrivKey(), PicryptLib.KEY_STORE + name.replace(' ', '_') + ".key");
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

