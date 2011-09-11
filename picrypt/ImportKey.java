package picrypt;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.event.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

public class ImportKey implements ActionListener, DocumentListener {
  private JFrame frame = null;
  private Container container = null;

  private JTextField name = null;
  private JTextArea pubKey = null;
  
  public ImportKey(JFrame frame, Container container) {
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
		container.add(new JLabel("Contact name:"), gridProps);

		gridProps = new GridBagConstraints();
		gridProps.gridx = 1;
		gridProps.gridy = 0;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		name = new JTextField(30);
    container.add(name, gridProps);
    
    gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 1;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		container.add(Util.setupButton(this, "Save Contact"), gridProps);
		
		gridProps = new GridBagConstraints();
		gridProps.gridx = 0;
		gridProps.gridy = 2;
		gridProps.gridwidth = 2;
		gridProps.fill = GridBagConstraints.BOTH;
		gridProps.weighty = 1;
		pubKey = new JTextArea();
		pubKey.setLineWrap(true);
    pubKey.setWrapStyleWord(false);
    pubKey.getDocument().addDocumentListener(this);
    container.add(new JScrollPane(pubKey), gridProps);
		
		frame.setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
  }
  
  public void insertUpdate(DocumentEvent e) {
    try {
      byte[] key = Base64.decode(pubKey.getText());
      int firstNameChar = 0;
      if (key.length >= PicryptLib.PUB_KEY_SIZE + AES.IV_LENGTH + PicryptLib.PRIV_KEY_SIZE) {
        firstNameChar = PicryptLib.PUB_KEY_SIZE + AES.IV_LENGTH + PicryptLib.PRIV_KEY_SIZE;
      }
      else {
        firstNameChar = PicryptLib.PUB_KEY_SIZE;
      }
      
      String suggestedName = new String(PicryptLib.sliceArray(key, firstNameChar, key.length));
      suggestedName = suggestedName.replace('_', ' ');
      name.setText(suggestedName);
    }
    catch (Exception ex) {}
  }
    
  public void removeUpdate(DocumentEvent e) {}
  
  public void changedUpdate(DocumentEvent e) {}
}

