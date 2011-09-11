package picrypt;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

public class Util {
  public static JButton setupButton(ActionListener al, String buttonText) {
		JButton button = new JButton(buttonText);
		button.setActionCommand(buttonText);
		button.addActionListener(al);
		return button;
  }
  
  public static JComboBox setupKeyNameDropDown() {
    String[] keys = (new File(PicryptLib.KEY_STORE)).list();
		for (int i=0; i<keys.length; i++) { 
		  keys[i] = keys[i].substring(0, keys[i].length() - 4).replace('_', ' ');
		}
		java.util.Arrays.sort(keys, String.CASE_INSENSITIVE_ORDER);
		
		return new JComboBox(keys);
  }
  
  public static boolean charEquals(char[] pwd1, char[] pwd2) {
    if (pwd1.length != pwd2.length) {
      return false;
    }
    
    for (int i=0; i<pwd1.length; i++) {
      if (pwd1[i] != pwd2[i]) {
        return false;
      }
    }
    
    return true;
  }
  
  public static void clearMemory(char[] data) {
    for (int i=0; i<data.length; i++) {
      data[i] = 0;
    }
  }
}

