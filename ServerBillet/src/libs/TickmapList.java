/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author 'Toine
 */
public class TickmapList {
    private List<TickmapClient> listtickmap = null;

    public TickmapList() {
        listtickmap = new LinkedList<>();
    }
    
    public synchronized void addTMClient(TickmapClient tc){
        listtickmap.add(tc);
    }
    
    public synchronized TickmapClient getTMClient(){
        TickmapClient tc=null;
        try{
            tc = listtickmap.remove(0);
        }
        catch(Exception e){
            
        }
        return tc;
    }
    
}
