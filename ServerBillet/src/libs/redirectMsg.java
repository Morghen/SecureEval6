/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.io.IOException;
import java.io.OutputStream;
import javax.swing.JTextArea;

/**
 *
 * @author Morghen
 */
public class redirectMsg extends OutputStream {
    
    private JTextArea zonetxt = null;
    
    public redirectMsg(JTextArea user) {
        this.zonetxt = user;
    }

    @Override
    public void write(int b) throws IOException {
        zonetxt.append(String.valueOf((char)b));
    }
    
    
    
}
