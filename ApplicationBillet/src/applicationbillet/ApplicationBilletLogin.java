/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationbillet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import libs.TickmapClient;
import static libs.libSecure.KeystoreAccess;
import protocole.TICKMAPTYPE;
import protocole.tickmap;

/**
 *
 * @author Morghen
 */
public class ApplicationBilletLogin extends javax.swing.JFrame {
    
    public TickmapClient tc = null;
    
    private PublicKey keyServ = null;
    private PrivateKey keyCli = null;
    private SecretKey keySecret = null;
    private Cipher chiffrement = null;
    
    private KeyStore ks = null;
    
    public boolean connected = false;
    
    
    public ApplicationBilletLogin() {
        initComponents();
    }
    
    public ApplicationBilletLogin(TickmapClient ptc) {
        //Security.addProvider(new BouncyCastleProvider());
        initComponents();
        setLocationRelativeTo(null);
        tc = ptc;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        OKButton = new javax.swing.JButton();
        LoginField = new javax.swing.JTextField();
        PasswdField = new javax.swing.JPasswordField();
        LoginLabel = new javax.swing.JLabel();
        PasswdLabel = new javax.swing.JLabel();
        CancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Application Billet");

        OKButton.setText("OK");
        OKButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OKButtonActionPerformed(evt);
            }
        });

        LoginLabel.setText("Login :");

        PasswdLabel.setText("Mot de passe :");

        CancelButton.setText("Annuler");
        CancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CancelButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(61, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(OKButton)
                    .addComponent(LoginLabel)
                    .addComponent(PasswdLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(LoginField)
                        .addComponent(PasswdField, javax.swing.GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE))
                    .addComponent(CancelButton))
                .addContainerGap(61, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(70, 70, 70)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(LoginField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(LoginLabel))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PasswdField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(PasswdLabel))
                .addGap(31, 31, 31)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(OKButton)
                    .addComponent(CancelButton))
                .addContainerGap(53, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void OKButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OKButtonActionPerformed
        System.out.println("Tentative de login");
        tickmap msgtickmap = new tickmap(TICKMAPTYPE.CONNECT);
        
        
        //on prends le login et password pour faire le msg
        String login = LoginField.getText();
        String mdp = PasswdField.getText();
        String msg = "";
        
        //on fait le truc crypto ici     
        System.out.println("Instanciation du message digest");
        byte[] msgD = null;
        try
        {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.update(mdp.getBytes());
            long temps = (new Date().getTime());
            double alea = Math.random();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream bdos = new DataOutputStream(baos);
            bdos.writeLong(temps);
            bdos.writeDouble(alea);
            md.update(baos.toByteArray());
            msg = ""+login + "#" + temps + "#" + alea + "#" + new String(md.digest());
        }
        catch(IOException ex) {
            Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        catch(NoSuchAlgorithmException ex) {
            Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
        //envois du msg
        msgtickmap.setMessage(msg);
        tc.write(msgtickmap);
        
        
        tickmap response = tc.read();
        
        if(response.getType() == TICKMAPTYPE.OK){
            
            System.out.println("Connecter OK");
            System.out.println("Debut handshake");
            tickmap msghandshake = new tickmap(TICKMAPTYPE.HANDSHAKE, " ");
            // Chargement du keystore
            ks = KeystoreAccess();
            try {
                KeyGenerator keyGen = KeyGenerator.getInstance("AES");
                keyGen.init(128);
                keySecret = keyGen.generateKey();
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                keyCli = (PrivateKey)ks.getKey("cleclient","ggbrogg".toCharArray());
                System.out.println("Cle privee recuperee");
                X509Certificate certifServ = (X509Certificate)ks.getCertificate("authserv");
                keyServ = certifServ.getPublicKey();
            } catch (KeyStoreException ex) {
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnrecoverableKeyException ex) {
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                chiffrement = Cipher.getInstance("RSA/ECB/PKCS1Padding");
                chiffrement.init(Cipher.ENCRYPT_MODE, keyServ);
            } catch (NoSuchAlgorithmException ex) {
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchPaddingException ex) {
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (InvalidKeyException ex) {        
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Creation message");
            
            byte[] bytesMsg = keySecret.getEncoded();
            System.out.println("Envoi du message" + new String(bytesMsg));
            try {
                byte[] msgCrypt = chiffrement.doFinal(bytesMsg);
                msg = new String(msgCrypt);
            } catch (IllegalBlockSizeException ex) {
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            } catch (BadPaddingException ex) {
                Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
            }

            msghandshake.setMessage(msg);
            System.out.println("Envoi du message = " + msghandshake.toString());
            
            tc.write(msghandshake);
            
            response = tc.read();
            connected = true;
        }
        else{
            connected = false;
            System.out.println("Connecter FAILED");
        }
        System.out.println("reponse = " + response.toString());
    }//GEN-LAST:event_OKButtonActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        System.out.println("Deconnection");
        System.exit(1);
    }//GEN-LAST:event_CancelButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ApplicationBilletLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ApplicationBilletLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ApplicationBilletLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ApplicationBilletLogin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ApplicationBilletLogin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton CancelButton;
    private javax.swing.JTextField LoginField;
    private javax.swing.JLabel LoginLabel;
    private javax.swing.JButton OKButton;
    private javax.swing.JPasswordField PasswdField;
    private javax.swing.JLabel PasswdLabel;
    // End of variables declaration//GEN-END:variables

}
