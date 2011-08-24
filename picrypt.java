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
  
  public static void main(String args[]) {
    Picrypt picrypt = new Picrypt();
  }
}

