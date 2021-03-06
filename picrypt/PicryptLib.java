package picrypt;

import java.io.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.KeyFactory;

import java.security.spec.X509EncodedKeySpec;
import java.security.spec.PKCS8EncodedKeySpec;

public abstract class PicryptLib {	  	
  public static final String KEY_STORE = "keys/";
  
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
    suggestedName = "picrypt." + suggestedName;
    return suggestedName;
  }
  
  public static double maxEmbedSize(String imgPath) {
    StegImg stegimg = new StegImg(imgPath);
    return stegimg.maxEmbedSize();
  }
  
  public static String maxEmbedSizeString(String imgPath) {
    StegImg stegimg = new StegImg(imgPath);
    return stegimg.maxEmbedSizeString();
  }
  
  public static  PublicKey getPubKey(String path) {
    byte[] keyAsBytes = getFileAsBytes(path);
    byte[] pubKeyAsBytes = sliceArray(keyAsBytes, 0, PUB_KEY_SIZE);
    
    try {
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		  X509EncodedKeySpec pubSpec = new X509EncodedKeySpec(pubKeyAsBytes);
          
		  return keyFactory.generatePublic(pubSpec);
		}
		catch (Exception e) { e.printStackTrace(); }
		
    return null;
  }
  
  public static void saveKey(char[] password, PublicKey pubKey, PrivateKey privKey, String outFile) {
    byte[] aesKey = AES.passwordToKey(password);
    
    byte[] rawPrivKey = privKey.getEncoded();
        
    AES aes = new AES();
    aes.setKey(aesKey);
    rawPrivKey = aes.encrypt(rawPrivKey);
    byte[] iv = aes.getIv();
    byte[] aesPriv = catArrays(aes.getIv(), rawPrivKey);
    
    (new File(KEY_STORE)).mkdir();
    byte[] toFile = catArrays(pubKey.getEncoded(), aesPriv);
    saveFile(outFile, toFile);
  }
  
  public static PrivateKey getPrivKey(char[] password, String path) {
    try {
      byte[] aesKey = AES.passwordToKey(password);
      byte[] keyAsBytes = getFileAsBytes(path);

      byte[] iv = sliceArray(keyAsBytes, PUB_KEY_SIZE, PUB_KEY_SIZE + AES.IV_LENGTH);
      byte[] data = sliceArray(keyAsBytes, PUB_KEY_SIZE + AES.IV_LENGTH, PUB_KEY_SIZE + AES.IV_LENGTH + PRIV_KEY_SIZE); 
      
      AES aes = new AES();
      aes.setKey(aesKey);
      aes.setIv(iv);
      data = aes.decrypt(data);
      
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		  PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(data);
          
		  return keyFactory.generatePrivate(privSpec);
		}
		catch (Exception e) { System.out.println("Nothing to see here... Or maybe the wrong password."); }
		
		return null;
  }
  public static String getRawPublicKeyB64(String name, String path) {
    byte[] key = catArrays(sliceArray(getFileAsBytes(path), 0, PUB_KEY_SIZE), name.getBytes());
    return Base64.encodeBytes(key);
  }
  
  public static String getRawKeyB64(String name, String path) {
    byte[] key = catArrays(getFileAsBytes(path), name.getBytes());
    return Base64.encodeBytes(key);
  }
  
  public static byte[] getFileAsBytes(String path) {
    FileInputStream inputfile = null;
    
    try {
      inputfile = new FileInputStream(path);
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

