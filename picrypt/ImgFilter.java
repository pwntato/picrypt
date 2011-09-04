package picrypt;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ImgFilter extends FileFilter {

  public boolean accept(File f) {
    if (f.isDirectory())
      return true;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {
      if (s.substring(i + 1).toLowerCase().equals("jpeg" ) || s.substring(i + 1).toLowerCase().equals("jpg" )  || s.substring(i + 1).toLowerCase().equals("png" )) {
        return true;
      }
    }

    return false;
  }

  public String getDescription() {
    return "*.jpeg, *.png";
  }
}
