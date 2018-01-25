/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationbillet;

import com.sun.glass.ui.Cursor;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import libs.TickmapClient;

/**
 *
 * @author 'Toine
 */
public class main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ApplicationBilletLogin applog = null;
        ApplicationBilletGUI appgui = null;
        TickmapClient tc = null;
        Socket CSocket;
        try {
            CSocket = new Socket("127.0.0.1",9025);
            System.out.println("Client connecte : "+CSocket.getInetAddress().toString());
            tc = new TickmapClient(CSocket);
            System.out.println("DIS & DOS acquis");
        } catch (IOException ex) {
            Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        applog = new ApplicationBilletLogin(tc);
        applog.setVisible(true);
        while(applog.connected == false){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        applog.setVisible(false);
        appgui = new ApplicationBilletGUI(tc);
        appgui.secretForCrypt = applog.keySecret;
        appgui.setVisible(true);
    }
    
}
