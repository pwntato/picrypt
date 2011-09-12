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

public class EmbedImage implements ActionListener {
  private JFrame frame = null;
  private Container container = null;

  private JComboBox keyNames = null;
  
  private JButton saveAsButton = null;
  
  private JTextField toHideName = null;
  private JTextField imgHideName = null;
  private JTextField imgSaveName = null;
  
  private File fileToHide = null;
  private File imgToHideIn = null;
  private File imgToSave = null;
  
  public EmbedImage(JFrame frame, Container container) {
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
		gridProps.fill = GridBagConstraints.HORIZONTAL;
		gridProps.anchor = GridBagConstraints.LINE_END;
		container.add(Util.setupButton(this, "File to hide"), gridProps);
		
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
		container.add(Util.setupButton(this, "Image to hide in"), gridProps);
		
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
		container.add(Util.setupButton(this, "Save as"), gridProps);
		
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
		container.add(Util.setupButton(this, "Hide File"), gridProps);
		
		frame.setVisible(true);
  }
  
	public void actionPerformed(ActionEvent e) {
	  if ("File to hide".equals(e.getActionCommand())) {
      JFileChooser fc = new JFileChooser();
      int returnVal = fc.showOpenDialog(frame);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        fileToHide = fc.getSelectedFile();
        
        toHideName.setText(fileToHide.getName());
      }
    }
    else if ("Image to hide in".equals(e.getActionCommand())) {
      JFileChooser fc = new JFileChooser();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      fc.addChoosableFileFilter(new ImgFilter(false));
      int returnVal = fc.showOpenDialog(frame);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        imgToHideIn = fc.getSelectedFile();
        imgHideName.setText(imgToHideIn.getName());
      }
    }
    else if ("Save as".equals(e.getActionCommand())) {
      JFileChooser fc = new JFileChooser();
      fc.removeChoosableFileFilter(fc.getAcceptAllFileFilter());
      fc.addChoosableFileFilter(new ImgFilter(true));
      fc.setSelectedFile(new File("picrypt.png"));
      int returnVal = fc.showSaveDialog(frame);
      
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        imgToSave = fc.getSelectedFile();
        imgSaveName.setText(imgToSave.getName());
      }
    }
    else if ("Hide File".equals(e.getActionCommand())) {
      if (fileToHide == null) {
        JOptionPane.showMessageDialog(frame, "You must select a file to hide");
      }
      else if (imgToHideIn == null) {
        JOptionPane.showMessageDialog(frame, "You must select an image to hide in");
      }
      else if (imgToSave == null) {
        JOptionPane.showMessageDialog(frame, "You must select where to save the new image");
      }
      else {
        PublicKey publicKey = PicryptLib.getPubKey(PicryptLib.KEY_STORE + ((String)keyNames.getSelectedItem()).replace(' ', '_') + ".key");
        PicryptLib.embedFile(publicKey, fileToHide.getPath(), imgToHideIn.getPath(), imgToSave.getPath());
        
        JOptionPane.showMessageDialog(frame, "Done!");
      }
    }
	}
}


