/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbillet;

import Data.Vols;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import libs.BDUtilities;
import libs.TickmapClient;
import libs.TickmapList;
import libs.Tracable;
import org.bouncycastle.util.encoders.Base64;
import protocole.TICKMAPTYPE;
import protocole.tickmap;
import sun.misc.BASE64Decoder;

/**
 *
 * @author Morghen
 */
public class ClientThread extends Thread{
    
    private TickmapList tl = null;
    private TickmapClient tc = null;
    private boolean running = false;
    private Tracable pere = null;
    private BDUtilities uti=null;
    private KeyStore ks = null;
    private SecretKey cleSecrete = null;

    public ClientThread(TickmapList ptc, Tracable t) {
        tl = ptc;
        pere = t;
        try {
            uti = new BDUtilities("localhost", 5500);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    @Override
    public void run() {
        running = true;
        while(running){
            tc = tl.getTMClient();
            int prixVols = 100;
            if(tc != null){
                //on a un client donc on peut excecuter ici les fcts
                pere.Trace("ThCli: on a un nouveau client");
                boolean connect = true;
                boolean crypt = false;
                int idClient = 0;
                int idVols = 0;
                int nbrBillet=0;
                while(connect ){
                    int taille = 0;
                    byte[] tmp = null;
                    tickmap msg = tc.read();
                    tickmap msgToSend = new tickmap(TICKMAPTYPE.NOK, " ");
                    LinkedList<Vols> lv = new LinkedList<>();
                    ResultSet rs=null;
                    StringTokenizer strTok=null;
                    crypt = false;
                    //traitement du msg
                    //c'est ici qu'on va faire les fonctions du protocol !
                    if(idClient != 0)
                        pere.Trace("ThCli recv "+idClient+" - "+msg.toString());
                    else
                        pere.Trace("ThCli recv - "+msg.toString());
                    switch(msg.getType()){
                        case CONNECT:
                            strTok = new StringTokenizer(msg.getMessage(), "#");
                            String login = strTok.nextToken();
                            long temps = Long.parseLong(strTok.nextToken());
                            double alea = Double.parseDouble(strTok.nextToken());
                            String digest = strTok.nextToken();
                            String mdp = null;
                            try {
                                rs = uti.query("SELECT idClient, password FROM client WHERE identifiant like '"+login+"'");
                                rs.next();
                                mdp = rs.getString("password");
                                idClient = rs.getInt("idClient");
                            } catch (Exception ex) {
                                //Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if(mdp != null){
                                //le mdp existe
                                MessageDigest md=null;
                                try {
                                    md = MessageDigest.getInstance("SHA-1");
                                    md.update(mdp.getBytes());
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    DataOutputStream bdos = new DataOutputStream(baos);
                                    bdos.writeLong(temps);
                                    bdos.writeDouble(alea);
                                    md.update(baos.toByteArray());
                                } catch (NoSuchAlgorithmException ex ) {
                                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //verif que les deux digest sont les même
                                //pere.Trace(login+" -- "+mdp);
                                String dig = new String(md.digest());
                                //pere.Trace(dig);
                                //pere.Trace(digest);
                                if(dig.equals(digest)){
                                    msgToSend.setType(TICKMAPTYPE.OK);
                                    msgToSend.setMessage("indentification OK");
                                }else{
                                    msgToSend.setMessage("MOT DE PASSE incorrect");
                                }
                            }
                            else{
                                //mdp inexistant
                                msgToSend.setMessage("mdp inexistant");
                            }
                            break;
                        case DISCONECT:
                            idClient =0;
                            connect = false;
                            break;
                        case HANDSHAKE:
                            ks = KeystoreAccess();
                            PrivateKey myKey = null;
                            Cipher decryptage = null;
                            try {
                                myKey = (PrivateKey)ks.getKey("cleserv","ggbrogg".toCharArray());
                            } catch (KeyStoreException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (NoSuchAlgorithmException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (UnrecoverableKeyException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                decryptage = Cipher.getInstance("RSA/ECB/NoPadding");
                                decryptage.init(Cipher.DECRYPT_MODE, myKey);
                            } catch (NoSuchAlgorithmException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (NoSuchPaddingException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (InvalidKeyException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            pere.Trace("Msg cle = "+msg.getMessage());
                            byte[] msgCrypt = msg.getMessage().getBytes();
                            byte[] msgDecrypt = null;
                            try {
                                msgDecrypt = decryptage.doFinal(msgCrypt);
                            } catch (IllegalBlockSizeException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (BadPaddingException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }                          
                            cleSecrete = new SecretKeySpec(msgDecrypt,0,msgDecrypt.length,"DES");
                            
                            msgToSend = new tickmap(TICKMAPTYPE.OK,"Handshake OK");
                            break;
                                     
                        case GETLISTVOL:
                            lv = new LinkedList<>();
                            rs=null;
                            try {
                                rs = uti.query("SELECT * FROM vols");
                            } catch (SQLException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            try {
                                while(rs.next()){
                                    lv.add(new Vols(rs));
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            try {
                                ObjectOutputStream oos = new ObjectOutputStream(baos);
                                oos.writeObject(lv);
                                oos.flush();
                            } catch (IOException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            String msgList = new String(Base64.encode(baos.toByteArray()));
                            msgToSend = new tickmap(TICKMAPTYPE.GETLISTVOL, msgList);
                            crypt = true;
                            break;
                        case ACHAT:
                            try {
                                strTok = new StringTokenizer(msg.getMessage(),"#");
                                idVols = Integer.parseInt(strTok.nextToken());
                                nbrBillet = Integer.parseInt(strTok.nextToken());

                                rs = uti.query("SELECT * FROM vols WHERE idVols = "+idVols);
                                rs.next();
                                
                                Vols v = new Vols(rs);
                                if(v.getNbrDispo() < nbrBillet){
                                    msgToSend = new tickmap(TICKMAPTYPE.NOK, "pas assez de billet dispo");
                                }else{
                                    int place = v.getNbrBillet() - v.getNbrDispo();
                                    int placeFin = place + nbrBillet;
                                    int prix = nbrBillet * prixVols;
                                    String str = ""+place+"#"+placeFin+"#"+prix+"#"+idClient+"#"+idVols;
                                    msgToSend = new tickmap(TICKMAPTYPE.OK, str);
                                }
                            } catch (SQLException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                                msgToSend.setMessage(ex.getMessage());
                            }
                            crypt=true;
                            break;
                        case CONFIRMATION:
                            try {
                                
                                int idTicket = 1;
                                rs = uti.query("SELECT max(idTicket) from ticket");
                                if(rs.next())
                                    idTicket = rs.getInt(1);
                                else
                                    idTicket = 1;
                                uti.update("UPDATE vols SET nbrDispo = nbrDispo-"+nbrBillet+" WHERE idVols = "+idVols);
                                for(int j=0; j<nbrBillet;j++)
                                {
                                    uti.update("INSERT INTO ticket(idTicket, idClient, idVols, payer) VALUES("+ ++idTicket +","+idClient+","+idVols+",'N')");
                                }
                                msgToSend.setType(TICKMAPTYPE.OK);
                            } catch (SQLException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                                msgToSend.setMessage(ex.getMessage());
                            }
                            crypt=true;
                            break;
                        case PAYEMENT:
                            strTok = new StringTokenizer(msg.getMessage(),"#");
                            idClient = Integer.parseInt(strTok.nextToken());
                            idVols = Integer.parseInt(strTok.nextToken());
                            try {
                                uti.update("UPDATE ticket SET payer = \"Y\" WHERE idClient = "+idClient+" AND idVols = "+idVols);
                            } catch (SQLException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            crypt=true;
                            break;
                        case NOTPAYEMENT:
                            strTok = new StringTokenizer(msg.getMessage(),"#");
                            idClient = Integer.parseInt(strTok.nextToken());
                            idVols = Integer.parseInt(strTok.nextToken());
                            int nbrBillet1 = 0;
                            try {
                                rs = uti.query("SELECT count(*) FROM ticket WHERE idClient = "+idClient+" AND idVols = "+idVols+" AND payer like 'N'");
                                rs.next();
                                nbrBillet = rs.getInt(1);
                                uti.update("DELETE FROM ticket WHERE idClient = "+idClient+" AND idVols = "+idVols+" AND payer like 'N'");
                                uti.update("UPDATE vols SET nbrDispo = nbrDispo-"+nbrBillet1+" WHERE idVols = "+idVols);
                            } catch (SQLException ex) {
                                Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            crypt =true;
                            break;
                        case NOTCONFIRM:
                            idVols=0;
                            nbrBillet=0;
                            crypt =true;
                            break;
                        default:
                            
                    }
                    pere.Trace("ThCli envois : "+msgToSend.toString());
                    if(crypt == true)
                        tc.write(msgToSend, cleSecrete);
                    else
                        tc.write(msgToSend);
                }
                pere.Trace("ThCli: fin client");
            }
        }
    }
    
    
    public void DoStop(){
        running = false;
    }

    private KeyStore KeystoreAccess() {
        KeyStore ks = null;
        try {
            ks = KeyStore.getInstance("JKS");
        } catch (KeyStoreException ex) {
            System.out.println("Erreur de keystore : "+ex);
        }
        char[] keyStorePassword = "test1234".toCharArray();
        try
        {
            InputStream ksis = new FileInputStream("keystore_serv.jks");
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
