/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.net.Socket;

/**
 *
 * @author 'Toine
 */
public class TickmapClient {
    
    private Socket clientSoc;

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
