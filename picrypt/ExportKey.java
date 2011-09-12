package picrypt;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ExportKey implements ActionListener {
  private JFrame frame = null;
  private Container container = null;

  private JComboBox keyNames = null;
  private JComboBox keyType = null;
  private JTextArea pubKey = null;
  
  public ExportKey(JFrame frame, Container container) {
    this.frame = frame;
    this.container = container;
  }
  
  public void setupDlg() {
    container.removeAll();
    frame.setSize(510, 370);
    container.repaint();
  
    GridBagConstraints gridProps = null;
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 0;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(new JLabel("Contact to export:"), gridProps);

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
		container.add(new JLabel("Export type:"), gridProps);
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 1;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		String[] types = {"Contact Info (Share with everyone)", "Secret Key (Encrypted, but keep it safe)"};
    keyType = new JComboBox(types);
    container.add(keyType, gridProps);
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 2;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		container.add(Util.setupButton(this, "Export"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 3;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.BOTH;
		gridProps.weighty = 1;
		pubKey = new JTextArea();
		pubKey.setLineWrap(true);
    pubKey.setWrapStyleWord(false);
    pubKey.setEditable(false);
    pubKey.setText("\n\n\n\n\n\n\n\n\n\n\n\n");
    container.add(new JScrollPane(pubKey), gridProps);
		
		frame.setVisible(true);
  }
  
	public void actionPerformed(ActionEvent e) {
	  if ("Export".equals(e.getActionCommand())) {
      String keyName = ((String)keyNames.getSelectedItem()).replace(' ', '_');
      String keyPath = PicryptLib.KEY_STORE + keyName + ".key";
      
      if (((String)keyType.getSelectedItem()).startsWith("Secret Key")) {
        pubKey.setText(PicryptLib.getRawKeyB64(keyName, keyPath));
      }
      else {
        pubKey.setText(PicryptLib.getRawPublicKeyB64(keyName, keyPath));
      }
    }
	}
}
  
