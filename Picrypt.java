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
    
    PicryptLib.embedFile(rsa.getPubKey(), "sample.doc", "sample.jpg", "output.png");       
       
    PicryptLib.extractFile(rsa.getPrivKey(), "output.png", "output.doc");
  }
}

