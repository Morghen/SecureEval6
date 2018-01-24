/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import protocole.payp;

/**
 *
 * @author 'Toine
 */
public class PaypClient {
    private Socket clientSoc;
    public DataInputStream in;
    public DataOutputStream out;
    

    public PaypClient(Socket pclientSoc) {
        this.clientSoc = pclientSoc;
        try {
            in = new DataInputStream(clientSoc.getInputStream());
            out = new DataOutputStream(clientSoc.getOutputStream());
            
        } catch (IOException ex) {
            Logger.getLogger(TickmapClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void write(payp t){
        try {
            out.writeInt(t.getLength());
            out.write(t.getBytes());
        } catch (Exception ex) {
            Logger.getLogger(TickmapClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public payp read() {
        payp ret = null;
        try {
            int taille = in.readInt();
            byte[] bytes = new byte[taille];
            in.read(bytes);
            ret = new payp(new String(bytes));
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
