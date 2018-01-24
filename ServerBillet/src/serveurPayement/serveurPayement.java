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
import libs.TickmapClient;
import libs.Tracable;

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
    
    public serveurPayement(Tracable zonetxt) {
        SSocket = null;
        pere = zonetxt;
        
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
        while(Running ){
            pere.Trace("ThServ : Serveur en attente");
            try
            {
                tc = new TickmapClient(SSocket.accept());
                
                pere.Trace("ThServ :Serveur a recu la connexion");
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
