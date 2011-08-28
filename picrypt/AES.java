package picrypt;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES 
{
  public static final int KEY_LENGTH = 32;
  public static final int IV_LENGTH = 16;
  
  private byte[] key = null;
  private byte[] iv = null;
  private SecretKeySpec k = null;
  
  public AES() {}
  
  public static byte[] passwordToKey(String password) {
    try {
      MessageDigest sha = MessageDigest.getInstance("SHA-256");
      
      sha.update(password.getBytes(), 0, password.length());
      byte[] sha_password = sha.digest();
      
      return sha_password;
    } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
    
    return null;
  }
  
  public byte[] decrypt(byte[] data) {
    try {
      Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));
      return c.doFinal(data);
    } catch (Exception e) { e.printStackTrace(); }
    
    return null;
  }
  
  public byte[] encrypt(byte[] data) {
    try {
      Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      c.init(Cipher.ENCRYPT_MODE, k);
      iv = c.getIV();
      return c.doFinal(data);
    } catch (Exception e) { e.printStackTrace(); }
    
    return null;
  }
  
  public void generateKey() {
    KeyGenerator kgen = null;
    
    try{
      kgen = KeyGenerator.getInstance("AES");
    } catch (Exception ex)  {}
    
    kgen.init(256); 
    SecretKey skey = kgen.generateKey();
    key = skey.getEncoded();
    k = new SecretKeySpec(key, "AES");
  }

  public byte[] getKeyAndIv(int length) {
    byte[] keyAndIv = new byte[KEY_LENGTH+IV_LENGTH+4];
    byte[] bLength = intToByteArray(length);
    
    for (int i=0; i<KEY_LENGTH+IV_LENGTH+4; i++) {
      if (i < KEY_LENGTH) {
        keyAndIv[i] = key[i];
      }
      else if (i < KEY_LENGTH + IV_LENGTH) {
        keyAndIv[i] = iv[i-KEY_LENGTH];
      }
      else {
        keyAndIv[i] = bLength[i-KEY_LENGTH-IV_LENGTH];
      }
    }
    
    return keyAndIv;
  }
  
  public int setKeyAndIv(byte[] keyAndIv) {
    byte[] bLength = new byte[4];
    key = new byte[KEY_LENGTH];
    iv = new byte[IV_LENGTH];
    
    for (int i=0; i<KEY_LENGTH+IV_LENGTH+4; i++) {
      if (i < KEY_LENGTH) {
        key[i] = keyAndIv[i];
      }
      else if (i < KEY_LENGTH + IV_LENGTH) {
        iv[i-KEY_LENGTH] = keyAndIv[i];
      }
      else {
        bLength[i-KEY_LENGTH-IV_LENGTH] = keyAndIv[i];
      }
    }
    k = new SecretKeySpec(key, "AES");
    
    return byteArrayToInt(bLength);
  }
  
  public byte[] intToByteArray(int value) {
    return new byte[] { (byte)(value >>> 24), (byte)(value >>> 16), (byte)(value >>> 8), (byte)value };
  }
  
  public int byteArrayToInt(byte [] data) {
    return (data[0] << 24) + ((data[1] & 0x000000ff) << 16) + ((data[2] & 0x000000ff) << 8) + (data[3] & 0x000000ff);
  }

  public byte[] getKey() {
    return key;
  }
  
  public void setKey(byte[] key) {
    this.key = key;
    k = new SecretKeySpec(key, "AES");
  }
  
  public byte[] getIv() {
    return iv;
  }
  
  public void setIv(byte[] iv) {
    this.iv = iv;
  }
    
  public SecretKeySpec getKeySpec() {
    return k;
  }
}

