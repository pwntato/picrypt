package picrypt;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class RSA 
{
  public static final int MAX_PAYLOAD = 256;
  public static final int HEADER_LENGTH = 128;
  
  private PublicKey pubKey = null;
  private PrivateKey privKey = null;
  
  public RSA() {}
  
  public byte[] encrypt(byte[] data) {
    try {
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.ENCRYPT_MODE, pubKey);
      return cipher.doFinal(data);
    } catch (Exception e)  { e.printStackTrace(); }
    
    return null; 
  }
  
  public byte[] decrypt(byte[] data) {
    try {
      Cipher cipher = Cipher.getInstance("RSA");
      cipher.init(Cipher.DECRYPT_MODE, privKey);
      return cipher.doFinal(data);
    } catch (Exception e)  { e.printStackTrace(); }
    
    return null;
  }
  
  public void generateKeyPair() {
     try {
      KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
      KeyPair keys = kpg.generateKeyPair();
      
      pubKey = keys.getPublic();
      privKey = keys.getPrivate();
    } catch (Exception e)  { e.printStackTrace(); }
  }

  public PublicKey getPubKey() {
    return pubKey;
  }

  public void setPubKey(PublicKey pubKey) {
    this.pubKey = pubKey;
  }

  public PrivateKey getPrivKey() {
    return privKey;
  }

  public void setPrivKey(PrivateKey privKey) {
    this.privKey = privKey;
  }
}

