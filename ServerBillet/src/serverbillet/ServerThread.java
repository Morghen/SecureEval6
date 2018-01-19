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

/**
 *
 * @author Morghen
 */
public class ServerThread implements Runnable {  
    
    private final int Port = 9025;
    private ServerSocket SSocket;
    private Socket CSocket;
    private ServerBilletGUI pere = null;
    
    public ServerThread(ServerBilletGUI Pere) {
        SSocket = null;
        CSocket = null;
        pere = Pere;
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
        System.out.println("Serveur en attente");
        
        try
        {
            CSocket = SSocket.accept();
        }
        catch(SocketException e)
        {
            System.out.println("Accept interrompu : "+e);
        }
        catch(IOException e)
        {
            System.err.println("Erreur accept : "+e);
            System.exit(-1);
        }
        
        try
        {
            System.out.println("Serveur a recu la connexion");
            DataInputStream dis = new DataInputStream(CSocket.getInputStream());
            DataOutputStream dos = new DataOutputStream(CSocket.getOutputStream());
                      
        }
        catch(EOFException e)
        {
            System.err.println("Le client a termine la connection");
        }
        catch(IOException e)
        {
            System.err.println("Erreur : "+e);
        }
    }

    public static void main(String args[]) {
        (new Thread(new ServerThread(new ServerBilletGUI()))).start();
    }
}
