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

