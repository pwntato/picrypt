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

public class AES {
  public static final int KEY_SIZE = 256;               // or 128
  public static final int KEY_LENGTH = KEY_SIZE / 8;
  public static final int IV_LENGTH = 16;
  public static final String HASH_ALGO = "SHA-256";     // use "MD5" for 128
  
  private byte[] key = null;
  private byte[] iv = null;
  private SecretKeySpec k = null;
  
  public AES() {}
  
  public static byte[] passwordToKey(char[] password) {
    try {
      byte[] passwordAsBytes = new byte[password.length];
      for (int i=0; i<password.length; i++) {
        passwordAsBytes[i] = (byte) password[i];
      }
      
      MessageDigest hash = MessageDigest.getInstance(HASH_ALGO);
      
      hash.update(passwordAsBytes, 0, password.length);
      byte[] hash_password = hash.digest();
      
      for (int i=0; i<passwordAsBytes.length; i++) {
        passwordAsBytes[i] = 0;
      }
      
      return hash_password;
    } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
    
    return null;
  }
  
  public byte[] decrypt(byte[] data) {
    try {
      Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
      c.init(Cipher.DECRYPT_MODE, k, new IvParameterSpec(iv));
      return c.doFinal(data);
    } catch (Exception e) {}
    
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
    
    kgen.init(KEY_SIZE); 
    SecretKey skey = kgen.generateKey();
    key = skey.getEncoded();
    k = new SecretKeySpec(key, "AES");
  }

  public byte[] getKeyAndIv() {
    byte[] keyAndIv = new byte[KEY_LENGTH+IV_LENGTH];
    
    for (int i=0; i<KEY_LENGTH+IV_LENGTH; i++) {
      if (i < KEY_LENGTH) {
        keyAndIv[i] = key[i];
      }
      else {
        keyAndIv[i] = iv[i-KEY_LENGTH];
      }
    }
    
    return keyAndIv;
  }
  
  public void setKeyAndIv(byte[] keyAndIv) {
    key = new byte[KEY_LENGTH];
    iv = new byte[IV_LENGTH];
    
    for (int i=0; i<KEY_LENGTH+IV_LENGTH; i++) {
      if (i < KEY_LENGTH) {
        key[i] = keyAndIv[i];
      }
      else {
        iv[i-KEY_LENGTH] = keyAndIv[i];
      }
    }
    k = new SecretKeySpec(key, "AES");
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

