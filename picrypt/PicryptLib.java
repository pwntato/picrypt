package picrypt;

import java.io.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyFactory;

import java.security.spec.X509EncodedKeySpec;

public abstract class PicryptLib
{	  
  public static final int PUB_KEY_SIZE = 550;

  public static void embedFile(PublicKey pubKey, String fileInPath, String imgInPath, String imgOutPath) {
    RSA rsa = new RSA();
    rsa.setPubKey(pubKey);
  
    AES aes = new AES();
    aes.generateKey();
  
    StegImg stegimgout = new StegImg(imgInPath);
    
    byte[] data = getFileAsBytes(fileInPath);
    data = aes.encrypt(data);
    
    int length = data.length;
    
    byte[] keyAndIv = aes.getKeyAndIv(length);
    keyAndIv = rsa.encrypt(keyAndIv);
    
    stegimgout.embedBytes(keyAndIv, 0, RSA.HEADER_LENGTH);
    stegimgout.embedBytes(data, byteCountToPixelCount(RSA.HEADER_LENGTH), length);
    stegimgout.saveImg(imgOutPath);
  }
  
  public static void extractFile(PrivateKey privKey, String imgPath, String filePath) {
    StegImg stegimgin = new StegImg(imgPath);
    byte[] header = stegimgin.extractBytes(0, RSA.HEADER_LENGTH);
    
    RSA rsa = new RSA();
    rsa.setPrivKey(privKey);
    header = rsa.decrypt(header);
    
    AES aes = new AES();
    int length = aes.setKeyAndIv(header);
       
    byte[] data = stegimgin.extractBytes(byteCountToPixelCount(RSA.HEADER_LENGTH), length);      
    data = aes.decrypt(data);
    saveFile(filePath, data);
  }
  
  public static void embedPubKey(PublicKey pubKey, String imgInPath, String imgOutPath) {  
    StegImg stegimgout = new StegImg(imgInPath);
    
    byte[] data = pubKey.getEncoded();
    
    stegimgout.embedBytes(data, 0, data.length);
    stegimgout.saveImg(imgOutPath);
  }
  
  public static PublicKey extractPubKey(String imgPath) {
    try {
      StegImg stegimgin = new StegImg(imgPath);

      byte[] data = stegimgin.extractBytes(0, PUB_KEY_SIZE); 
      
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		  X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(data);
          
		  return keyFactory.generatePublic(pubSpec);
		}
		catch (Exception e) { e.printStackTrace(); }
		
		return null;
  }
  
  public static byte[] getFileAsBytes(String path) {
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
  
  public static void saveFile(String path, byte[] data) {
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
  
  public static int byteCountToPixelCount(int byteCount) {
    double pixelCount = byteCount * (8.0 / 6.0);
    return pixelCount % 1 == 0.0 ? (int)pixelCount : (int)pixelCount + 1;
  }
}

