/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbillet;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.BDUtilities;
import libs.TickmapClient;
import libs.TickmapList;
import libs.Tracable;
import protocole.TICKMAPTYPE;
import protocole.tickmap;

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
            if(tc != null){
                //on a un client donc on peut excecuter ici les fcts
                pere.Trace("ThCli: on a un nouveau client");
                boolean connect = true;
                while(connect ){
                    int taille = 0;
                    byte[] tmp = null;
                    tickmap msg = tc.read();
                    tickmap msgToSend = new tickmap(TICKMAPTYPE.NOK);
                    //traitement du msg
                    //c'est ici qu'on va faire les fonctions du protocol !
                    pere.Trace("Type = "+msg.getType()+" "+msg.getMessage());
                    switch(msg.getType()){
                        case CONNECT:
                            StringTokenizer strTok = new StringTokenizer(msg.getMessage(), "#");
                            String login = strTok.nextToken();
                            long temps = Long.parseLong(strTok.nextToken());
                            double alea = Double.parseDouble(strTok.nextToken());
                            String mdp = null;
                            try {
                                ResultSet rs = uti.query("SELECT password FROM client WHERE identifiant like '"+login+"'");
                                rs.next();
                                mdp = rs.getString(1);
                            } catch (Exception ex) {
                                //Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if(mdp != null){
                                //le mdp existe
                                MessageDigest md;
                                try {
                                    md = MessageDigest.getInstance("SHA-1");

                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    DataOutputStream bdos = new DataOutputStream(baos);
                                    bdos.writeLong(temps);
                                    bdos.writeDouble(alea);
                                    md.update(baos.toByteArray());
                                } catch (NoSuchAlgorithmException ex) {
                                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                                } catch (IOException ex) {
                                    Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                //verif que les deux digest sont les même
                            }
                            else{
                                //mdp inexistant
                                msgToSend.setMessage("mdp inexistant");
                            }
                            break;
                        case DISCONECT:
                            connect = false;
                            break;
                        case ACHAT:
                            break;
                    }
                    tc.write(msgToSend);
                }
                pere.Trace("ThCli: fin client");
            }
        }
    }
    
    
    public void DoStop(){
        running = false;
    }
}
