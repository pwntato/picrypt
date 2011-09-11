package picrypt;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

public class CreateKey implements ActionListener {
  private JFrame frame = null;
  private Container container = null;

  private JTextField name = null;
  private JPasswordField newPassword1 = null;
  private JPasswordField newPassword2 = null;
  private JTextArea pubKey = null;
  
  public CreateKey(JFrame frame, Container container) {
    this.frame = frame;
    this.container = container;
  }
  
  public void setupDlg() {
    container.removeAll();
    container.repaint();
    frame.setSize(510, 370);
  
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
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		container.add(Util.setupButton(this, "Create Contact Info"), gridProps);
		
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
		
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
	  if ("Create Contact Info".equals(e.getActionCommand())) {
      char[] pwd1 = newPassword1.getPassword();
      char[] pwd2 = newPassword2.getPassword();
      if (Util.charEquals(pwd1, pwd2)) {
        createKey(name.getText(), pwd1);
              
        Util.clearMemory(pwd1);
        Util.clearMemory(pwd2);
      }
      else {
        JOptionPane.showMessageDialog(frame, "Passwords don't match");
      }
    }
	}
	
	public void createKey(String name, char[] password) {
    RSA rsa = new RSA();
    rsa.generateKeyPair();
    
    byte[] rawPub = PicryptLib.catArrays(rsa.getPubKey().getEncoded(), name.getBytes());
    
    pubKey.setText(Base64.encodeBytes(rawPub));
    
    PicryptLib.saveKey(password, rsa.getPubKey(), rsa.getPrivKey(), PicryptLib.KEY_STORE + name.replace(' ', '_') + ".key");
  }
}

