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

public class ExtractImage implements ActionListener {
  private JFrame frame = null;
  private Container container = null;

  private JComboBox keyNames = null;
  private JPasswordField newPassword1 = null;
  
  private JButton saveAsButton = null;
  
  private JTextField imgHideName = null;
  private JTextField fileSaveName = null;
  
  private File imgToHideIn = null;
  private File fileToSave = null;
  
  private JFileChooser fc;
  
  public ExtractImage(JFrame frame, Container container) {
    this.frame = frame;
    this.container = container;
    
    fc = new JFileChooser();
  }
  
  public void setupDlg() {
    container.removeAll();
    container.repaint();
    frame.setSize(500, 175);
  
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
		keyNames = Util.setupKeyNameDropDown();
    container.add(keyNames, gridProps);    
  
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
		container.add(Util.setupButton(this, "Image to decrypt"), gridProps);
		
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
		saveAsButton = Util.setupButton(this, "Save file as");
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
		container.add(Util.setupButton(this, "Extract File"), gridProps);
		
		frame.setVisible(true);
  }
  
  public void actionPerformed(ActionEvent e) {
    if ("Image to decrypt".equals(e.getActionCommand())) {
      fc.resetChoosableFileFilters();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      fc.setSelectedFile(new File(""));
      fc.addChoosableFileFilter(new ImgFilter(true));
      int returnVal = fc.showOpenDialog(frame);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        imgToHideIn = fc.getSelectedFile();
        char[] password = newPassword1.getPassword();
        if (password.length > 0) {
          PrivateKey privateKey = PicryptLib.getPrivKey(password, PicryptLib.KEY_STORE + ((String)keyNames.getSelectedItem()).replace(' ', '_') + ".key");
          
          if (privateKey != null) {
            fileToSave = new File(PicryptLib.getSuggestedFileName(privateKey, imgToHideIn.getPath()));
            imgHideName.setText(imgToHideIn.getName());
            fileSaveName.setText(fileToSave.getName());
            saveAsButton.setEnabled(true);
          }
          else {
            JOptionPane.showMessageDialog(frame, "Incorrect password");
          }
          
          Util.clearMemory(password);
        }
        else {
          JOptionPane.showMessageDialog(frame, "Enter your password first");
        }
      }      
    }
    else if ("Save file as".equals(e.getActionCommand())) {
      fc.resetChoosableFileFilters();
      fc.setSelectedFile(new File(""));
      int returnVal = fc.showSaveDialog(frame);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        fileToSave = fc.getSelectedFile();
        fileSaveName.setText(fileToSave.getName());
      }
    }
    else if ("Extract File".equals(e.getActionCommand())) {
      char[] password = newPassword1.getPassword();
      if (password.length > 0) {
        PrivateKey privateKey = PicryptLib.getPrivKey(password, PicryptLib.KEY_STORE + ((String)keyNames.getSelectedItem()).replace(' ', '_') + ".key");
        
        if (privateKey != null) {
          PicryptLib.extractFile(privateKey, imgToHideIn.getPath(), fileToSave.getPath());
          Util.clearMemory(password);
          JOptionPane.showMessageDialog(frame, "Done!");
        }
        else {
          JOptionPane.showMessageDialog(frame, "Incorrect password");
        }
      } 
      else {
        JOptionPane.showMessageDialog(frame, "Enter your password first");
      }
    }
  }
}

