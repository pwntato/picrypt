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
    
    for (int x=0; x < img.getWidth(); x++) {
      for (int y=0; y < img.getHeight(); y++) {
        int[] pixel = getPixel(x, y);
        
        pixel[0] |= 0x00000003;
        pixel[1] |= 0x00000003;
        pixel[2] |= 0x00000003;
        
        setPixel(x, y, pixel);
      }
    }
    
    try {
      File outputfile = new File("output.png");
      ImageIO.write(img, "png", outputfile);
    } catch (IOException e) { System.out.println(e); }
    
    try {
        img = ImageIO.read(new File("output.png"));
    } catch (IOException e) { System.out.println(e); }
    
    for (int x=0; x < img.getWidth(); x++) {
      for (int y=0; y < img.getHeight(); y++) {
        int[] pixel = getPixel(x, y);
        
        pixel[0] &= 0x00000003;
        pixel[1] &= 0x00000003;
        pixel[2] &= 0x00000003;
        
        System.out.println("Red: " + pixel[0] + ", Green: " + pixel[1] + ", Blue: " + pixel[2]);
      }
    }
  }
  
  public int[] getPixel(int x, int y) {
    int MASK = 0x000000ff;
    int raw = img.getRGB(x, y);
    int[] pixel = new int[3];
    
    pixel[0] = (raw >> 16) & MASK;
    pixel[1] = (raw >> 8) & MASK;
    pixel[2] = raw & MASK;
    
    return pixel;
  }
  
  public void setPixel(int x, int y, int[] rgb) {
    int pixel = 0xff000000;
    
    pixel |= (rgb[0] << 16);
    pixel |= (rgb[1] << 8);
    pixel |= rgb[2];
    
    img.setRGB(x, y, pixel);
  }
  
  public static void main(String args[]) {
    Picrypt picrypt = new Picrypt();
  }
}

