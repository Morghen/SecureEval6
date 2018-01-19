/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverbillet;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import javax.swing.JTextArea;

/**
 *
 * @author Morghen
 */
public class ServerThread implements Runnable {  
    
    private final int Port = 9025;
    private ServerSocket SSocket;
    private Socket CSocket;
    private ServerBilletGUI pere = null;
    
    public ServerThread(ServerBilletGUI zonetxt) {
        SSocket = null;
        CSocket = null;
        pere = zonetxt;
    }
               
    @Override
    public void run() {
        try
        {
            SSocket = new ServerSocket(Port);
        }
        catch(IOException e)
        {
            pere.Trace("Erreur port d'ecoute : "+e);
            System.exit(-1);
        }
        pere.Trace("Serveur en attente");
        
        try
        {
            CSocket = SSocket.accept();
        }
        catch(SocketException e)
        {
            pere.Trace("Accept interrompu : "+e);
        }
        catch(IOException e)
        {
            pere.Trace("Erreur accept : "+e);
            System.exit(-1);
        }
        
        try
        {
            pere.Trace("Serveur a recu la connexion");
            DataInputStream dis = new DataInputStream(CSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
                      
        }
        catch(EOFException e)
        {
            pere.Trace("Le client a termine la connection");
        }
        catch(IOException e)
        {
            pere.Trace("Erreur : "+e);
        }
    }

    public static void main(String args[]) {
        (new Thread(new ServerThread(new ServerBilletGUI()))).start();
    }
}
