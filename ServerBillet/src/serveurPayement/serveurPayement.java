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
import libs.PaypClient;
import libs.TickmapClient;
import libs.Tracable;
import protocole.TICKMAPTYPE;
import protocole.payp;
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
    public PaypClient pc =null;
    public BDUtilities uti=null;
    public TickmapClient tc =null;
    
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
            
            tc = new TickmapClient(new Socket("localhost", 9025));
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
                pc = new PaypClient(SSocket.accept());
                pere.Trace("ThServ :Serveur a recu la connexion");
                
                payp request = pc.read();
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
                            tc.write(new tickmap(TICKMAPTYPE.PAYEMENT,""+idVols+"#"+idClient));
                            tc.read();
                            break;
                        case NOTPAYEMENT:
                            strTok = new StringTokenizer(request.getMessage(),"#");
                            idVols = Integer.parseInt(strTok.nextToken());
                            idClient = Integer.parseInt(strTok.nextToken());
                            tc.write(new tickmap(TICKMAPTYPE.NOTPAYEMENT,""+idVols+"#"+idClient));
                            tc.read();
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
