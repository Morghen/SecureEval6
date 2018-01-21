/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbillet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import libs.TickmapClient;
import libs.TickmapList;
import libs.Tracable;
import protocole.tickmap;

/**
 *
 * @author Morghen
 */
public class ServerThread extends Thread {  
    
    private final int Port = 9025;
    private final int NbrCliTh = 3;
    private boolean Running = false;
    private ServerSocket SSocket;
    private Tracable pere = null;
    
    private TickmapList tickmapList = null;
    private List<ClientThread> listCli = null;
    
    public ServerThread(Tracable zonetxt) {
        SSocket = null;
        pere = zonetxt;
        
        listCli = new LinkedList<>();
        tickmapList = new TickmapList();
        
        try
        {
            SSocket = new ServerSocket(Port);
        }
        catch(IOException e)
        {
            pere.Trace("ThServ : Erreur port d'ecoute : "+e);
        }
        listCli = new LinkedList<>();
        for(int i=0; i<NbrCliTh; i++){
            listCli.add(new ClientThread(tickmapList, pere));
        }
    }
               
    @Override
    public void run() {
        Running = true;
        while(Running ){
            pere.Trace("ThServ : Serveur en attente");
            try
            {
                TickmapClient tc = new TickmapClient(SSocket.accept());
                tickmapList.addTMClient(tc);
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

    public static void main(String args[]) {
        (new Thread(new ServerThread(new ServerBilletGUI()))).start();
    }
}
