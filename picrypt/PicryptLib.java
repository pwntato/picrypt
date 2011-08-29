package picrypt;

import java.io.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyFactory;

import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public abstract class PicryptLib
{	  
  public static final int PUB_KEY_SIZE = 550;
  public static final int PRIV_KEY_SIZE = 2384;

  public static void embedFile(PublicKey pubKey, String fileInPath, String imgInPath, String imgOutPath) {
    RSA rsa = new RSA();
    rsa.setPubKey(pubKey);
  
    AES aes = new AES();
    aes.generateKey();
  
    StegImg stegimgout = new StegImg(imgInPath);
    
    byte[] data = getFileAsBytes(fileInPath);
    data = aes.encrypt(data);
    
    int length = data.length;
    
    byte[] header = aes.getKeyAndIv();
    header = catArrays(header, intToByteArray(length));
    
    header = catArrays(header, (new File(fileInPath)).getName().getBytes());
    header = rsa.encrypt(header);
    
    stegimgout.embedBytes(header, 0, RSA.HEADER_LENGTH);
    stegimgout.embedBytes(data, byteCountToPixelCount(RSA.HEADER_LENGTH), length);
    stegimgout.saveImg(imgOutPath);
  }
  
  public static void extractFile(PrivateKey privKey, String imgPath, String filePath) {
    StegImg stegimgin = new StegImg(imgPath);
    
    byte[] header = getHeader(privKey, stegimgin);
    
    AES aes = new AES();
    aes.setKeyAndIv(sliceArray(header, 0, AES.KEY_LENGTH + AES.IV_LENGTH));
    int length = byteArrayToInt(sliceArray(header, AES.KEY_LENGTH + AES.IV_LENGTH, AES.KEY_LENGTH + AES.IV_LENGTH + 4));
       
    byte[] data = stegimgin.extractBytes(byteCountToPixelCount(RSA.HEADER_LENGTH), length);      
    data = aes.decrypt(data);
    saveFile(filePath, data);
  }
  
  public static byte[] getHeader(PrivateKey privKey, String imgPath) {
    StegImg stegimgin = new StegImg(imgPath);
    return getHeader(privKey, stegimgin);
  }
  
  public static byte[] getHeader(PrivateKey privKey, StegImg stegimgin) {
    byte[] header = stegimgin.extractBytes(0, RSA.HEADER_LENGTH);
    
    RSA rsa = new RSA();
    rsa.setPrivKey(privKey);
    return rsa.decrypt(header);
  }
  
  public static String getSuggestedFileName(PrivateKey privKey, String imgPath) {
    byte[] header = getHeader(privKey, imgPath);
    String suggestedName = new String(sliceArray(header, AES.KEY_LENGTH + AES.IV_LENGTH + 4, header.length));
    suggestedName = "output." + suggestedName;
    return suggestedName;
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
  
  public static void embedKey(String password, PublicKey pubKey, PrivateKey privKey, String imgInPath, String imgOutPath) {  
    byte[] aesKey = AES.passwordToKey(password);
    StegImg stegimgout = new StegImg(imgInPath);
    
    byte[] data = pubKey.getEncoded();
    
    stegimgout.embedBytes(data, 0, data.length);
    
    data = privKey.getEncoded();
        
    AES aes = new AES();
    aes.setKey(aesKey);
    data = aes.encrypt(data);
    byte[] iv = aes.getIv();
    
    stegimgout.embedBytes(iv, byteCountToPixelCount(PUB_KEY_SIZE), iv.length);
    stegimgout.embedBytes(data, byteCountToPixelCount(PUB_KEY_SIZE + AES.IV_LENGTH ), data.length);
    stegimgout.saveImg(imgOutPath);
  }
  
  public static PrivateKey extractPrivKey(String password, String imgPath) {
    try {
      byte[] aesKey = AES.passwordToKey(password);
      StegImg stegimgin = new StegImg(imgPath);

      byte[] iv = stegimgin.extractBytes(byteCountToPixelCount(PUB_KEY_SIZE), AES.IV_LENGTH);
      byte[] data = stegimgin.extractBytes(byteCountToPixelCount(PUB_KEY_SIZE + AES.IV_LENGTH), PRIV_KEY_SIZE); 
      
      AES aes = new AES();
      aes.setKey(aesKey);
      aes.setIv(iv);
      data = aes.decrypt(data);
      
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		  PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(data);
          
		  return keyFactory.generatePrivate(privSpec);
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
  
  public static byte[] catArrays(byte[] arr1, byte[] arr2) {
    byte[] out = new byte[arr1.length+arr2.length];
    for (int i=0; i<arr1.length+arr2.length; i++) {
      if (i < arr1.length) {
        out[i] = arr1[i];
      }
      else {
        out[i] = arr2[i-arr1.length];
      }
    }
    return out;
  }
  
  public static byte[] sliceArray(byte[] arr, int start, int end) {
    byte[] slice = new byte[end-start];
    for (int i=start; i<end; i++) {
      slice[i-start] = arr[i];
    }
    return slice;
  }
  
  public static byte[] intToByteArray(int value) {
    return new byte[] { (byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value };
  }
  
  public static int byteArrayToInt(byte [] data) {
    return (data[0] << 24) + ((data[1] & 0x000000ff) << 16) + ((data[2] & 0x000000ff) << 8) + (data[3] & 0x000000ff);
  }
  
  public static int byteCountToPixelCount(int byteCount) {
    double pixelCount = byteCount * (8.0 / 6.0);
    return pixelCount % 1 == 0.0 ? (int)pixelCount : (int)pixelCount + 1;
  }
}

