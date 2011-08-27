import javax.swing.JFrame;
import java.io.*;
import java.io.FileInputStream;
import javax.imageio.*;
import java.awt.image.BufferedImage;

class Picrypt extends JFrame 
{	
  private BufferedImage img;
  private int position = 0;
  private int color_band = 0;

  public Picrypt() {
    try {
        img = ImageIO.read(new File("sample.jpg"));
    } catch (IOException e) { System.out.println(e); }
    
    FileInputStream in = null;
    byte[] input = new byte[1048576];
    int length = 0;
    try {
      in = new FileInputStream("sample.doc");
      length = in.read(input, 0, 1048576);
      
      embedBytes(input, 0, length);
    }
    catch (Exception e) { System.out.println(e); }
    finally {
      if (in != null) {
          try { in.close(); } catch (Exception e) {}
      }
    }
    
    try {
      File outputfile = new File("output.png");
      ImageIO.write(img, "png", outputfile);
    } catch (IOException e) { System.out.println(e); }
    
    try {
        img = ImageIO.read(new File("output.png"));
    } catch (IOException e) { System.out.println(e); }
    
    try {
      color_band = 0;
      byte[] data = extractBytes(0, length);
      FileOutputStream outputfile = new FileOutputStream("output.doc");
      outputfile.write(data, 0, length);
    } catch (IOException e) { System.out.println(e); }
  }
  
  public byte[] extractBytes(int offset, int length) {
    int position = offset;
    int x = position % img.getWidth();
    int y = position / img.getHeight();
    int[] pixel = getPixel(x, y);
    int bit_pair = 0;
    int working_byte = 0;
    byte[] data = new byte[length];
    
    for(int cur_byte=0; cur_byte<length; cur_byte++) {
      working_byte = 0; 
      for (int cur_bits=3; cur_bits>=0; cur_bits--) {
        if (color_band > 2) {           
          color_band = 0;
          position++;
        
          x = position % img.getWidth();
          y = position / img.getHeight();
          
          pixel = getPixel(x, y);
        }
        
        bit_pair = pixel[color_band];
        bit_pair &= 0x00000003;
        bit_pair <<= 2 * cur_bits;
        working_byte += bit_pair;
        
        color_band++;
      }
      data[cur_byte] = (byte)working_byte;
    }  
    
    return data;
  }
  
  public void printPixel(int x, int y, int[] pixel) {
    System.out.println("Pixel (" + x + ", " + y + "): " + pixel[0] + ", " + pixel[1] + ", " + pixel[2]);
  }
  
  public void embedBytes(byte[] data, int offset, int length) {
    int position = offset;
    int x = position % img.getWidth();
    int y = position / img.getHeight();
    int[] pixel = getPixel(x, y);
    int bit_pair;
    color_band = 0;
    
    for(int cur_byte=0; cur_byte<length; cur_byte++) {
      for (int cur_bits=3; cur_bits>=0; cur_bits--) {
        bit_pair = data[cur_byte] >> (2 * cur_bits);
        bit_pair &= 0x00000003;
           
        if (color_band > 2) {        
          color_band = 0;
          position++;
        
          x = position % img.getWidth();
          y = position / img.getHeight();
          
          pixel = getPixel(x, y);
        }
        
        pixel[color_band] &= 0xfffffffc;
        pixel[color_band] += bit_pair;
        setPixel(x, y, pixel);
        
        color_band++;
      }
    }
  }
  
  public void printBinary(int data) {
    String inBinary = "";
    for (int i=31; i>=0; i--) {
      inBinary += (data >> i) & 0x00000001;
    }
    System.out.println(inBinary);
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

