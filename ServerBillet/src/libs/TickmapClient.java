/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocole.tickmap;

/**
 *
 * @author 'Toine
 */
public class TickmapClient {
    
    private Socket clientSoc;
    public DataInputStream in;
    public DataOutputStream out;
    

    public TickmapClient(Socket pclientSoc) {
        this.clientSoc = pclientSoc;
        try {
            in = new DataInputStream(clientSoc.getInputStream());
            out = new DataOutputStream(clientSoc.getOutputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(TickmapClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void write(tickmap t){
        try {
            out.writeInt(t.getLength());
            out.write(t.getBytes());
        } catch (Exception ex) {
            Logger.getLogger(TickmapClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public tickmap read() {
        tickmap ret = null;
        try {
            int taille = in.readInt();
            byte[] bytes = new byte[taille];
            in.read(bytes);
            ret = new tickmap(new String(bytes));
        } catch (Exception ex) {
            Logger.getLogger(TickmapClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

    /**
     * Get the value of clientSoc
     *
     * @return the value of clientSoc
     */
    public Socket getClientSoc() {
        return clientSoc;
    }

    /**
     * Set the value of clientSoc
     *
     * @param clientSoc new value of clientSoc
     */
    public void setClientSoc(Socket clientSoc) {
        this.clientSoc = clientSoc;
    }

}
