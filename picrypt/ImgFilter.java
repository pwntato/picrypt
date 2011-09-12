package picrypt;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class ImgFilter extends FileFilter {
  private boolean pngOnly = false;
  
  public ImgFilter(boolean pngOnly) {
    this.pngOnly = pngOnly;
  }

  public boolean accept(File f) {
    if (f.isDirectory())
      return true;
    String s = f.getName();
    int i = s.lastIndexOf('.');

    if (i > 0 && i < s.length() - 1) {
      String extension = s.substring(i + 1).toLowerCase();
      if (extension.equals("png" ) || ((extension.equals("jpeg" ) || extension.equals("jpg" )) && !pngOnly)) {
        return true;
      }
    }

    return false;
  }

  public String getDescription() {
    if (pngOnly) {
      return "*.png";
    }
    else {
      return "*.jpeg, *.png";
    }
  }
}
