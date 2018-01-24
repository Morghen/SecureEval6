/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Morghen
 */
public class libSecure {
    
    private static KeyStore ks = null;
   
    public libSecure() {
        
    }
    
    public static void Handshake_Cli() {
        
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException ex) {
            System.out.println("Erreur de keystore : "+ex);
        }
        char[] keyStorePassword = "testcle1234".toCharArray();
        try
        {
            InputStream ksis = new FileInputStream("keystore.ks");
            ks.load(ksis, keyStorePassword);
        }
        catch(IOException ex)
        {
            System.out.println("Erreur d'ouverture du keystore : "+ex);           
        }
        catch(NoSuchAlgorithmException ex)
        {
            System.out.println("Erreur d'algorithme sur keystore : "+ex);
            System.exit(-1);
        }
        catch(CertificateException ex)
        {
            System.out.println("Erreur de certificats : "+ex);
            System.exit(-1);
        }
    }
    
    
}
