import javax.swing.JFrame;
import java.io.*;
import picrypt.*;

class Picrypt extends JFrame 
{	
  public Picrypt() {
    StegImg stegimgout = new StegImg("sample.jpg");
    FileInputStream inputfile = null;
    byte[] input = new byte[1048576];
    int length = 0;
    try {
      inputfile = new FileInputStream("sample.doc");
      length = inputfile.read(input, 0, 1048576);
      
      stegimgout.embedBytes(input, 0, length);
    }
    catch (Exception e) { System.out.println(e); }
    finally {
      if (inputfile != null) {
          try { inputfile.close(); } catch (Exception e) {}
      }
    }
    stegimgout.saveImg("output.png");
    
    StegImg stegimgin = new StegImg("output.png");
    FileOutputStream outputfile = null;    
    try {
      byte[] data = stegimgin.extractBytes(0, length);
      outputfile = new FileOutputStream("output.doc");
      outputfile.write(data, 0, length);
    } catch (IOException e) { System.out.println(e); }
    finally {
      if (outputfile != null) {
          try { outputfile.close(); } catch (Exception e) {}
      }
    }
  }
  
  public void printPixel(int x, int y, int[] pixel) {
    System.out.println("Pixel (" + x + ", " + y + "): " + pixel[0] + ", " + pixel[1] + ", " + pixel[2]);
  }
  
  public void printBinary(int data) {
    String inBinary = "";
    for (int i=31; i>=0; i--) {
      inBinary += (data >> i) & 0x00000001;
    }
    System.out.println(inBinary);
  }
  
  public static void main(String args[]) {
    Picrypt picrypt = new Picrypt();
  }
}

