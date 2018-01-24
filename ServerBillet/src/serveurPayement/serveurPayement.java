/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serveurPayement;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.BDUtilities;
import libs.TickmapClient;
import libs.Tracable;
import protocole.tickmap;

/**
 *
 * @author 'Toine
 */
public class serveurPayement extends Thread{
    private int Port = 9026;
    private boolean Running = false;
    private ServerSocket SSocket;
    private Tracable pere = null;
    public TickmapClient tc =null;
    public BDUtilities uti=null;
    
    public serveurPayement(Tracable zonetxt) {
        SSocket = null;
        pere = zonetxt;
        try {
            uti = new BDUtilities("localhost", 5500);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(serveurPayement.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(serveurPayement.class.getName()).log(Level.SEVERE, null, ex);
        }
        try
        {
            SSocket = new ServerSocket(Port);
            
        }
        catch(IOException e)
        {
            pere.Trace("ThServ : Erreur port d'ecoute : "+e);
        }
    }
               
    @Override
    public void run() {
        Running = true;
        boolean connected =true;
        while(Running ){
            pere.Trace("ThServ : Serveur en attente");
            try
            {
                tc = new TickmapClient(SSocket.accept());
                pere.Trace("ThServ :Serveur a recu la connexion");
                
                tickmap request = tc.read();
                int idVols = 0;
                int idClient =0;
                StringTokenizer strTok;
                pere.Trace("ThServ : "+request.toString());
                if(request != null){
                    switch(request.getType()){
                        case PAYEMENT:
                            strTok = new StringTokenizer(request.getMessage(),"#");
                            idVols = Integer.parseInt(strTok.nextToken());
                            idClient = Integer.parseInt(strTok.nextToken());
                            uti.update("UPDATE ticket SET payer = \"Y\" WHERE idClient = "+idClient+" AND idVols = "+idVols);
                            break;
                        case NOTPAYEMENT:
                            strTok = new StringTokenizer(request.getMessage(),"#");
                            idVols = Integer.parseInt(strTok.nextToken());
                            idClient = Integer.parseInt(strTok.nextToken());
                            int nbrBillet = 0;
                            ResultSet rs = uti.query("SELECT count(*) FROM ticket WHERE idClient = "+idClient+" AND idVols = "+idVols+" AND payer like 'N'");
                            rs.next();
                            nbrBillet = rs.getInt(1);
                            uti.update("DELETE FROM ticket WHERE idClient = "+idClient+" AND idVols = "+idVols+" AND payer like 'N'");
                            uti.update("UPDATE vols SET nbrDispo = nbrDispo-"+nbrBillet+" WHERE idVols = "+idVols);
                            break;
                    }
                }
            }
            catch(SocketException e)
            {
                pere.Trace("ThServ : Accept interrompu : "+e);
            }
            catch(IOException e)
            {
                pere.Trace("ThServ : Erreur accept : "+e);
            } catch (SQLException ex) {
                Logger.getLogger(serveurPayement.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    public void doStop() {
        try {
            Running = false;
            SSocket.close();
        } catch (IOException ex) {
            pere.Trace("ThServ : Erreur fermeture de connection : "+ex);
        }
    }
}
