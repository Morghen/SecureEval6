/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbillet;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.TickmapClient;
import libs.TickmapList;
import libs.Tracable;
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

    public ClientThread(TickmapList ptc, Tracable t) {
        tl = ptc;
        pere = t;
    }
    
    
    
    @Override
    public void run() {
        running = true;
        while(running){
            tc = tl.getTMClient();
            if(tc != null){
                //on a un client donc on peut excecuter ici les fcts
                boolean connect = true;
                while(connect ){
                    int taille = 0;
                    byte[] tmp = null;
                    tickmap msg = null;
                    
                    //lecture du msg
                    try {
                        taille = tc.in.readInt();
                        tmp = new byte[taille];
                        tc.in.readFully(tmp);
                        msg = new tickmap(tmp.toString());
                    } catch (IOException ex) {
                        Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    //traitement du msg
                    //c'est ici qu'on va faire les fonctions du protocol !
                    switch(msg.getType()){
                        case CONNECT:
                            break;
                        case DISCONECT:
                            connect = false;
                            break;
                        case ACHAT:
                            break;
                    }
                }
            }
        }
    }
    
    
    public void DoStop(){
        running = false;
    }
}
