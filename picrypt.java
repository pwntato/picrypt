import javax.swing.JFrame;
import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;

class Picrypt extends JFrame 
{	
  private BufferedImage img;

  public Picrypt() {
    try {
        img = ImageIO.read(new File("sample.jpg"));
    } catch (IOException e) { System.out.println(e); }
    
    getPixel(550, 550);
    
    int pixel = 0;
    int[] color = {0, 0, 0};
    for (int x=0; x < img.getWidth(); x++) {
      for (int y=0; y < img.getHeight(); y++) {
        pixel = getRgbAsInt(color);
        setPixel(x, y, pixel);
      }
    }
    
    try {
      File outputfile = new File("output.jpg");
      ImageIO.write(img, "jpg", outputfile);
    } catch (IOException e) { System.out.println(e); }
  }
  
  public int getRgbAsInt(int[] rgb) {
    int pixel = 0xff000000;
    
    pixel |= (rgb[0] << 16);
    pixel |= (rgb[1] << 8);
    pixel |= rgb[2];
    
    return pixel;
  }
  
  public int[] getPixel(int x, int y) {
    int MASK = 0x000000ff;
    int raw = img.getRGB(x, y);
    int[] pixel = new int[3];
    
    pixel[0] = (raw >> 16) & MASK;
    pixel[1] = (raw >> 8) & MASK;
    pixel[2] = raw & MASK;
    
    System.out.println("Red: " + pixel[0] + ", Green: " + pixel[1] + ", Blue: " + pixel[2]);
    
    return pixel;
  }
  
  public void setPixel(int x, int y, int pixel) {
    img.setRGB(x, y, pixel);
  }
  
  public static void main(String args[]) {
    Picrypt picrypt = new Picrypt();
  }
}

