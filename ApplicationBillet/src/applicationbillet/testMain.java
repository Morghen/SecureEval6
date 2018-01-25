/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationbillet;

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.Vector;

/**
 *
 * @author 'Toine
 */
public class testMain {

    public static void main(String[] args)
    {
        try
        {
            KeyStore ks = KeyStore.getInstance("JKS");
            ks.load(new FileInputStream("..\\keystore.jks"),
            "test1234".toCharArray());
            Enumeration en = ks.aliases();
            String aliasCourant = null;
            Vector vectaliases = new Vector();
            while (en.hasMoreElements()) vectaliases.add(en.nextElement());
            Object[] aliases = vectaliases.toArray();
            //OU : String[] aliases = (String []) (vectaliases.toArray(new String[0]));
            for (int i = 0; i < aliases.length; i++)
            {
                if (ks.isKeyEntry(aliasCourant=(String)aliases[i]))
                    System.out.println((i+1) + ".[keyEntry] " + aliases[i].toString());
                else
                if (ks.isCertificateEntry(aliasCourant))
                    System.out.println((i+1) + ".[trustedCertificateEntry] " + aliases[i].toString());
                X509Certificate certif = (X509Certificate)ks.getCertificate(aliasCourant);
                System.out.println("Type de certificat : " + certif.getType());
                System.out.println("Nom du propriétaire du certificat : " +certif.getSubjectDN().getName());
                System.out.println("Recuperation de la cle publique");
                PublicKey cléPublique = certif.getPublicKey();
                System.out.println("*** Cle publique recuperee = "+cléPublique.toString());
                System.out.println("Dates limites de validité : [" + certif.getNotBefore() + " - " + certif.getNotAfter() + "]");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    
}
