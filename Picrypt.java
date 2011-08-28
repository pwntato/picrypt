import javax.swing.JFrame;
import java.io.*;
import picrypt.*;

class Picrypt extends JFrame 
{	
  public Picrypt() {
    byte[] aesKey = AES.passwordToKey("test");
    byte[] iv = null;
    AES aes = new AES();
    aes.setKey(aesKey);
  
    StegImg stegimgout = new StegImg("sample.jpg");
    FileInputStream inputfile = null;
    int length = 0;
    try {
      inputfile = new FileInputStream("sample.doc");
      length = inputfile.available();
      byte[] data = new byte[length];
      inputfile.read(data, 0, length);
      
      data = aes.encrypt(data);
      iv = aes.getIv();
      length = data.length;
      
      stegimgout.embedBytes(data, 1000, length);
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
      byte[] data = stegimgin.extractBytes(1000, length);
      aes.setKey(aesKey);
      aes.setIv(iv);
      data = aes.decrypt(data);
      outputfile = new FileOutputStream("output.doc");
      outputfile.write(data, 0, data.length);
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

