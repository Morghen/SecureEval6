/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package applicationbillet;

import com.sun.glass.ui.Cursor;

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

        appgui = new ApplicationBilletGUI();
        appgui.setVisible(true);
    }
    
}
