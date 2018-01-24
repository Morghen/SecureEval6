/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package protocole;

import java.util.StringTokenizer;

/**
 *
 * @author 'Toine
 */
public class payp {
    private PAYPTYPE type;
    
    private String message;

    public payp(PAYPTYPE type) {
        this.type = type;
    }

    public payp(PAYPTYPE type, String message) {
        this.type = type;
        this.message = message;
    }
    
    public payp(String msg) {
        StringTokenizer strTok = new StringTokenizer(msg, "|");
        this.type = PAYPTYPE.valueOf(strTok.nextToken());
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
    public PAYPTYPE getType() {
        return type;
    }

    /**
     * Set the value of type
     *
     * @param type new value of type
     */
    public void setType(PAYPTYPE type) {
        this.type = type;
    }
    
    @Override
    public String toString(){
        return ""+type+"|"+message;
    }
    
    public byte[] getBytes(){
        return toString().getBytes();
    }
    public int getLength(){
        return toString().getBytes().length;
    }
}
