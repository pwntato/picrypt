import javax.swing.JFrame;
import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

class Picrypt extends JFrame 
{	
  private PublicKey pubKey = null;
  private PrivateKey privKey = null;
  
  public Picrypt() {
    RSA rsa = new RSA();
    rsa.generateKeyPair();
    pubKey = rsa.getPubKey();
    privKey = rsa.getPrivKey();
    
    embedFile("sample.doc", "sample.jpg", "output.png");       
       
    extractFile("output.png", "output.doc");
  }
  
  public void embedFile(String fileInPath, String imgInPath, String imgOutPath) {
    RSA rsa = new RSA();
    rsa.setPubKey(pubKey);
    rsa.setPrivKey(privKey);
  
    byte[] aesKey = AES.passwordToKey("test");
    AES aes = new AES();
    aes.setKey(aesKey);
  
    StegImg stegimgout = new StegImg(imgInPath);
    
    byte[] data = getFileAsBytes(fileInPath);
    data = aes.encrypt(data);
    
    int length = data.length;
    
    byte[] keyAndIv = aes.getKeyAndIv(length);
    keyAndIv = rsa.encrypt(keyAndIv);
    
    stegimgout.embedBytes(keyAndIv, 0, RSA.HEADER_LENGTH);
    stegimgout.embedBytes(data, 1000, length);
    stegimgout.saveImg(imgOutPath);
  }
  
  public void extractFile(String imgPath, String filePath) {
    StegImg stegimgin = new StegImg(imgPath);
    byte[] header = stegimgin.extractBytes(0, RSA.HEADER_LENGTH);
    
    RSA rsa = new RSA();
    rsa.setPubKey(pubKey);
    rsa.setPrivKey(privKey);
    header = rsa.decrypt(header);
    
    AES aes = new AES();
    int length = aes.setKeyAndIv(header);
       
    byte[] data = stegimgin.extractBytes(1000, length);      
    data = aes.decrypt(data);
    saveFile(filePath, data);
  }
  
  public byte[] getFileAsBytes(String path) {
    FileInputStream inputfile = null;
    
    try {
      inputfile = new FileInputStream("sample.doc");
      int length = inputfile.available();
      byte[] data = new byte[length];
      inputfile.read(data, 0, length);
      
      return data;
    }
    catch (Exception e) { e.printStackTrace(); }
    finally {
      if (inputfile != null) {
          try { inputfile.close(); } catch (Exception e) {}
      }
    }
    
    return null;
  }
  
  public void saveFile(String path, byte[] data) {
    FileOutputStream outputfile = null;    
    
    try {      
      outputfile = new FileOutputStream(path);
      outputfile.write(data, 0, data.length);
    } catch (IOException e) { System.out.println(e); }
    finally {
      if (outputfile != null) {
        try { outputfile.close(); } catch (Exception e) {}
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
  
  public static void main(String args[]) {
    Picrypt picrypt = new Picrypt();
  }
}

