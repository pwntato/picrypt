import javax.swing.JFrame;
import java.io.*;
import picrypt.*;

import java.security.PrivateKey;
import java.security.PublicKey;

class Picrypt extends JFrame 
{	  
  public static void main(String args[]) {
    RSA rsa = new RSA();
    rsa.generateKeyPair();
    
    PicryptLib.embedPubKey(rsa.getPubKey(), "sample.jpg", "output.pubKey.png");
    PicryptLib.embedPrivKey("password", rsa.getPrivKey(), "sample.jpg", "output.privKey.png");
    
    PicryptLib.embedFile(PicryptLib.extractPubKey("output.pubKey.png"), "sample.doc", "sample.jpg", "output.png");     
    
    PicryptLib.extractFile(PicryptLib.extractPrivKey("password", "output.privKey.png"), "output.png", "output.doc");
  }
}

