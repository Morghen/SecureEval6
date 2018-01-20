/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 'Toine
 */
public class TickmapClient {
    
    private Socket clientSoc;
    public InputStream in;
    public OutputStream out;
    

    public TickmapClient(Socket pclientSoc) {
        this.clientSoc = pclientSoc;
        try {
            in = clientSoc.getInputStream();
            out = clientSoc.getOutputStream();
        } catch (IOException ex) {
            Logger.getLogger(TickmapClient.class.getName()).log(Level.SEVERE, null, ex);
        }
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
