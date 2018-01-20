/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbillet;

import java.net.Socket;
import libs.TickmapClient;
import libs.TickmapList;

/**
 *
 * @author Morghen
 */
public class ClientThread extends Thread{
    
    private TickmapList tl = null;
    private TickmapClient tc = null;
    private boolean running = false;

    public ClientThread(TickmapList ptc) {
        tl = ptc;
    }
    
    
    
    @Override
    public void run() {
        running = true;
        while(running){
            tc = tl.getTMClient();
            if(tc != null){
                //on a un client donc on peut excecuter ici les fcts
                
            }
        }
    }
    
    
    public void DoStop(){
        running = false;
    }
}
