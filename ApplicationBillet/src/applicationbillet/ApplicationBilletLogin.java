/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationbillet;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.libUtils;
import protocole.TICKMAPTYPE;
import protocole.tickmap;

/**
 *
 * @author Morghen
 */
public class ApplicationBilletLogin extends javax.swing.JFrame {
    
    Socket CSocket = null;
    DataInputStream dis = null;
    DataOutputStream dos = null;
    
    public boolean connected = false;
    
    public ApplicationBilletLogin() {
        Security.addProvider(new BouncyCastleProvider());
        initComponents();
        setLocationRelativeTo(null);    
        Connect();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
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
            MessageDigest md = MessageDigest.getInstance("SHA-1","BC");
            md.update(login.getBytes());
            md.update(mdp.getBytes());
            long temps = (new Date().getTime());
            double alea = Math.random();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream bdos = new DataOutputStream(baos);
            bdos.writeLong(temps);
            bdos.writeDouble(alea);
            md.update(baos.toByteArray());
            msgD = md.digest();
            msg = login + "#" + Long.toString(temps) + "#" + Double.toString(alea) + "#" + Arrays.toString(msgD);
        }
        catch(IOException ex) {
            System.out.println("Erreur d'IO : "+ex);
        }
        catch(NoSuchAlgorithmException ex) {
            System.out.println("Erreur d'algorithme : "+ex);
        }
        catch(NoSuchProviderException ex) {
            System.out.println("Erreur de provider : "+ex);
        }
        //envois du msg
        msgtickmap.setMessage(msg);
        try {
            dos.writeInt(msgtickmap.getSize());
            dos.write(msgtickmap.toString().getBytes());
        } catch (IOException ex) {
            Logger.getLogger(ApplicationBilletLogin.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_OKButtonActionPerformed

    private void CancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CancelButtonActionPerformed
        System.out.println("Deconnection");
        Disconnect();
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

    private void Connect() {
        try 
        {
            CSocket = new Socket("127.0.0.1",9025);
            System.out.println("Client connecte : "+CSocket.getInetAddress().toString());
            dis = new DataInputStream(CSocket.getInputStream());
            dos = new DataOutputStream(CSocket.getOutputStream());
            System.out.println("DIS & DOS acquis");
        }
        catch (IOException ex)
        {
            System.err.println("Erreur, pas de connexion : "+ex);
        }       
    }

    private void Disconnect() {
        
        if(CSocket == null)
            System.exit(0);
        else
        {
            try 
            {
                CSocket.close();
                System.exit(0);
            } catch (IOException ex) {
                System.out.println("Erreur de deconnection : "+ex);
            }          
        }
    }
}
