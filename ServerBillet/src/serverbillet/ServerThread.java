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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextArea;
import libs.Tracable;

/**
 *
 * @author Morghen
 */
public class ServerThread extends Thread {  
    
    private final int Port = 9025;
    private ServerSocket SSocket;
    private Socket CSocket;
    private Tracable pere = null;
    private BufferedReader dis = null;
    private BufferedWriter dos = null;
    
    public ServerThread(Tracable zonetxt) {
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
            dis = new BufferedReader(new InputStreamReader(CSocket.getInputStream()));
            dos = new BufferedWriter(new OutputStreamWriter(CSocket.getOutputStream()));                    
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
    
    public void doStop() {
        try {
            SSocket.close();
        } catch (IOException ex) {
            pere.Trace("Erreur fermeture de connection : "+ex);
        }
    }

    public static void main(String args[]) {
        (new Thread(new ServerThread(new ServerBilletGUI()))).start();
    }
}
