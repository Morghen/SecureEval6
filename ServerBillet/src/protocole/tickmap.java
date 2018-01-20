/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocole;

import java.util.StringTokenizer;

/**
 *
 * @author Morghen
 */
public class tickmap {
    
    private TICKMAPTYPE type;
    
    private String message;

    public tickmap(TICKMAPTYPE type) {
        this.type = type;
    }

    public tickmap(TICKMAPTYPE type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public tickmap(String msg) {
        StringTokenizer strTok = new StringTokenizer(msg, "|");
        this.type = TICKMAPTYPE.valueOf(strTok.nextToken());
        this.message = strTok.nextToken();
    }

    /**
     * Get the value of message
     *
     * @return the value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the value of message
     *
     * @param message new value of message
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * Get the value of type
     *
     * @return the value of type
     */
    public TICKMAPTYPE getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(TICKMAPTYPE type) {
        this.type = type;
    }
    
    @Override
    public String toString(){
        return ""+type+"|"+message;
    }
    
    public int getSize(){
        return toString().length();
    }
}
