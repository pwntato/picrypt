package picrypt;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ChangePassword implements ActionListener {
  private JFrame frame = null;
  private Container container = null;

  private JComboBox keyNames = null;
  private JPasswordField currentPassword = null;
  private JPasswordField newPassword1 = null;
  private JPasswordField newPassword2 = null;
  
  public ChangePassword(JFrame frame, Container container) {
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
		container.add(new JLabel("Encrypt to:"), gridProps);

		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 0;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		keyNames = Util.setupKeyNameDropDown();
    container.add(keyNames, gridProps);
				
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 1;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Enter password:"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 1;
		currentPassword = new JPasswordField(30);
		container.add(currentPassword, gridProps);
				
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 2;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Enter new password:"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 2;
		newPassword1 = new JPasswordField(30);
		container.add(newPassword1, gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 3;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("New password again:"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 3;
		newPassword2 = new JPasswordField(30);
		container.add(newPassword2, gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 4;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		container.add(Util.setupButton(this, "Update password"), gridProps);
		
		frame.setVisible(true);
	}
	
	public void actionPerformed(ActionEvent e) {
    if ("Update password".equals(e.getActionCommand())) {
      char[] curPwd = currentPassword.getPassword();
      char[] pwd1 = newPassword1.getPassword();
      char[] pwd2 = newPassword2.getPassword();
      
      if (Util.charEquals(pwd1, pwd2)) {
        String keyPath = PicryptLib.KEY_STORE + ((String)keyNames.getSelectedItem()).replace(' ', '_') + ".key";
        PublicKey publicKey = PicryptLib.getPubKey(keyPath);
        
        PrivateKey privateKey = PicryptLib.getPrivKey(curPwd, keyPath);
        if (privateKey == null) {
          JOptionPane.showMessageDialog(frame, "Incorrect password");
        }
        else {        
          PicryptLib.saveKey(pwd1, publicKey, privateKey, keyPath);
          JOptionPane.showMessageDialog(frame, "Done!");
        }
      }
      else {
        JOptionPane.showMessageDialog(frame, "Passwords don't match");
      }
      
      Util.clearMemory(curPwd);
      Util.clearMemory(pwd1);
      Util.clearMemory(pwd2);
    }
  }
}
  
