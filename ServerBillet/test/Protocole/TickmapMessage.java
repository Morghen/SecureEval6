/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocole;

/**
 *
 * @author Morghen
 */
public class TickmapMessage {
    
    private TickmapType type = null;
    private Object charge = null;
    
    public TickmapMessage(TickmapType type, Object charge)
    {
        setType(type);
        setCharge(charge);
    }
    
    public TickmapMessage(TickmapType type)
    {
        setType(type);
    }

    /**
     * @return the type
     */
    public TickmapType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(TickmapType type) {
        this.type = type;
    }

    /**
     * @return the charge
     */
    public Object getCharge() {
        return charge;
    }

    /**
     * @param charge the charge to set
     */
    public void setCharge(Object charge) {
        this.charge = charge;
    }
    
    
    
}
