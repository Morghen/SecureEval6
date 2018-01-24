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
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
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
    
    public void setSKey(String alias,KeyStore ks,SecretKey sk,String mdp)
    {
        KeyStore.SecretKeyEntry ske = new KeyStore.SecretKeyEntry(sk);
        KeyStore.ProtectionParameter entryPass = new KeyStore.PasswordProtection(mdp.toCharArray());
        try {
            ks.setEntry(alias,ske,entryPass);
        } catch (KeyStoreException ex) {
            System.out.println("Erreur dans le set key du keystore : "+ex);
            System.exit(-1);
        }
    }
    
    public KeyStore.Entry getKey(KeyStore ks,String alias,String mdp)
    {
        KeyStore.Entry keyEntry = null;
        KeyStore.ProtectionParameter pass = new KeyStore.PasswordProtection(mdp.toCharArray());
        try {
            keyEntry = ks.getEntry(alias, pass);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("Erreur d'algorithme sur keystore : "+ex);
            System.exit(-1);;
        } catch (UnrecoverableEntryException ex) {
            System.out.println("Erreur de recuperation de la cle : "+ex);
            System.exit(-1);
        } catch (KeyStoreException ex) {
            System.out.println("Erreur dans le get key du keystore : "+ex);
            System.exit(-1);
        }
        
        return keyEntry;
    }
    
    public void KeystoreSave(KeyStore ks,String mdp)
    {
        char[] keyStorePassword = null;
        
        try
        {
            keyStorePassword = "test1234".toCharArray();
            FileOutputStream fos = new FileOutputStream("..\\keystore.ks");  
            ks.store(fos,keyStorePassword);
            fos.close();
        }
        catch(IOException ex)
        {
            System.out.println("Erreur de creation du keystore : "+ex);
        }
        catch(KeyStoreException ex)
        {
            System.out.println("Erreur dans le saveKeystore : "+ex);
        }
        catch(NoSuchAlgorithmException ex)
        {
            System.out.println("Erreur d'algorithme dans le save keystore : "+ex);
        }
        catch(CertificateException ex)
        {
            System.out.println("Erreur de certificat dans le save keystore : "+ex);
        }
    }
}
