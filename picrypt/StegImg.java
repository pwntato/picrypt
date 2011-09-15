package picrypt;

import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;

import java.text.*;

public class StegImg {
  private BufferedImage img;

  public StegImg(String imagePath) {
    try {
        img = ImageIO.read(new File(imagePath));
    } catch (IOException e) { System.out.println(e); }
  }
  
  public double maxEmbedSize() {
    double size = img.getWidth() * img.getHeight();    
    size *= 6.0/8.0;      // 6 bits per pixel, 8 bits per byte
    size -= RSA.HEADER_LENGTH;
    size -= 16;           // bonus iv block
    
    return size;
  }
  
  public String maxEmbedSizeString() {
    double size = maxEmbedSize();
    int magnitude = 0;
    String strSize = "";
    
    while (size > 1028) {
      size /= 1028;
      magnitude++;
    }
    
    DecimalFormat df = new DecimalFormat("#.##");
    strSize = df.format(size);
    switch (magnitude) {
      case 0:
        strSize += " B";
        break;
      case 1:
        strSize += " KB";
        break;
      case 2:
        strSize += " MB";
        break;
      case 3:
        strSize += " GB";
        break;        
    }
    
    return strSize;
  }
  
  public void saveImg(String imagePath) {
    try {
      ImageIO.write(img, "png", new File(imagePath));
    } catch (IOException e) { System.out.println(e); }
  }
  
  public void embedBytes(byte[] data, int offset, int length) {
    int position = offset;
    int x = position % img.getWidth();
    int y = position / img.getHeight();
    int[] pixel = getPixel(x, y);
    int bit_pair;
    int color_band = 0;
    
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
  
  public byte[] extractBytes(int offset, int length) {
    int position = offset;
    int x = position % img.getWidth();
    int y = position / img.getHeight();
    int[] pixel = getPixel(x, y);
    int color_band = 0;
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
}

