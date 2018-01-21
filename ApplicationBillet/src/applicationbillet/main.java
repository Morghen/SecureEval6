/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationbillet;

import com.sun.glass.ui.Cursor;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        
        applog = new ApplicationBilletLogin();
        applog.setVisible(true);
        while(applog.connected == false){
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        applog.setVisible(false);
        appgui = new ApplicationBilletGUI();
        appgui.setVisible(true);
    }
    
}
