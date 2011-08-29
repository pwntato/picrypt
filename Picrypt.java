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
    PicryptLib.embedKey("password", rsa.getPubKey(), rsa.getPrivKey(), "sample.jpg", "output.key.png");
    
    PicryptLib.embedFile(PicryptLib.extractPubKey("output.pubKey.png"), "~/java/picrypt/sample.doc", "sample.jpg", "output.png");     
    
    PrivateKey privKey = PicryptLib.extractPrivKey("password", "output.key.png");
    String suggestedFileName = PicryptLib.getSuggestedFileName(privKey, "output.png");
    PicryptLib.extractFile(privKey, "output.png", suggestedFileName);
  }
}

