/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationbillet;

import Data.Vols;
import com.sun.corba.se.impl.logging.OMGSystemException;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.PaypClient;
import libs.TickmapClient;
import protocole.PAYPTYPE;
import protocole.TICKMAPTYPE;
import protocole.payp;
import protocole.tickmap;

/**
 *
 * @author 'Toine
 */
public class ConfirmationAchat extends javax.swing.JDialog {

    public TickmapClient tc = null;
    public int prix=0;
    public int idClient =0;
    public int idVols =0;
    /**
     * Creates new form ConfirmationAchat
     */
    public ConfirmationAchat(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    public ConfirmationAchat(java.awt.Frame parent, boolean modal, String info, TickmapClient ptc) {
        super(parent, modal);
        initComponents();
        StringTokenizer strTok = new StringTokenizer(info,"#");
        int place = Integer.parseInt(strTok.nextToken());
        int placeFin = Integer.parseInt(strTok.nextToken());
        prix = Integer.parseInt(strTok.nextToken());
        idClient = Integer.parseInt(strTok.nextToken());
        idVols = Integer.parseInt(strTok.nextToken());
        String infoStr = "Place de "+place+" jusque "+placeFin+" pour un prix de "+prix+"";
        infoLabel.setText(infoStr);
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

        infoLabel = new javax.swing.JLabel();
        confirmButton = new javax.swing.JButton();
        annulerButton = new javax.swing.JButton();
        carteCreditLabel = new javax.swing.JLabel();
        carteCreditTF = new javax.swing.JTextField();
        nomLabel = new javax.swing.JLabel();
        nomTF = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        infoLabel.setText("jLabel1");

        confirmButton.setText("Confirmer");
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                confirmButtonMouseClicked(evt);
            }
        });

        annulerButton.setText("Annuler");
        annulerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                annulerButtonMouseClicked(evt);
            }
        });

        carteCreditLabel.setText("Carte Credit");

        carteCreditTF.setText(" ");

        nomLabel.setText("Nom Client :");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(confirmButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(annulerButton))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(nomLabel)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(75, 75, 75)
                        .addComponent(infoLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(carteCreditLabel)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nomTF, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(carteCreditTF, javax.swing.GroupLayout.PREFERRED_SIZE, 290, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 149, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(infoLabel)
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nomLabel)
                    .addComponent(nomTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(carteCreditLabel)
                    .addComponent(carteCreditTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(confirmButton)
                    .addComponent(annulerButton))
                .addGap(52, 52, 52))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void annulerButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_annulerButtonMouseClicked
        // TODO add your handling code here:
       tc.write(new tickmap(TICKMAPTYPE.NOTCONFIRM,"NOK"), tc.secretKeyForCrypt);
       tc.read(tc.secretKeyForCrypt);
       try {
            PaypClient pc = new PaypClient(new Socket("127.0.0.1",9026));
            pc.write(new payp(PAYPTYPE.NOTPAYEMENT, ""+idClient+"#"+idVols));
        } catch (IOException ex) {
            Logger.getLogger(ConfirmationAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
       this.dispose();
    }//GEN-LAST:event_annulerButtonMouseClicked

    private void confirmButtonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_confirmButtonMouseClicked
        // TODO add your handling code here:
        tc.write(new tickmap(TICKMAPTYPE.CONFIRMATION,"OK"), tc.secretKeyForCrypt);
        tc.read(tc.secretKeyForCrypt);
        try {
            PaypClient pc = new PaypClient(new Socket("127.0.0.1",9026));
            //envoyer n°carte credit crypter
            //nom client
            //montant
            //signature employer
            pc.write(new payp(PAYPTYPE.PAYEMENT, ""+idClient+"#"+idVols));
        } catch (IOException ex) {
            Logger.getLogger(ConfirmationAchat.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_confirmButtonMouseClicked

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
            java.util.logging.Logger.getLogger(ConfirmationAchat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ConfirmationAchat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ConfirmationAchat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ConfirmationAchat.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ConfirmationAchat dialog = new ConfirmationAchat(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton annulerButton;
    private javax.swing.JLabel carteCreditLabel;
    private javax.swing.JTextField carteCreditTF;
    private javax.swing.JButton confirmButton;
    private javax.swing.JLabel infoLabel;
    private javax.swing.JLabel nomLabel;
    private javax.swing.JTextField nomTF;
    // End of variables declaration//GEN-END:variables
}
