/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import javax.crypto.SecretKey;

/**
 *
 * @author Morghen
 */
public class libSecure {
       
    public libSecure() {
        
    }
    
    public static KeyStore KeystoreAccess() {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("JKS");
        } catch (KeyStoreException ex) {
            System.out.println("Erreur de keystore : "+ex);
        }
        char[] keyStorePassword = "test1234".toCharArray();
        try
        {
            InputStream ksis = new FileInputStream("..\\keystore_cli.jks");
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
        
        return ks;
    }
}
