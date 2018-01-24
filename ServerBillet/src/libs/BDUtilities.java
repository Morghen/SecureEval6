/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_INSENSITIVE;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;


/**
 *
 * @author 'Toine
 */
public class BDUtilities {
    private String nameConnection;
    private String login;
    private String motdepasse;
    private Connection con;
    private Statement instruc;

    //</editor-fold>
    public BDUtilities(String ip, int port) throws ClassNotFoundException, SQLException {
        String tmpCon;
        login = "user";
        motdepasse = "toor";
        
        nameConnection = "org.gjt.mm.mysql.Driver";
        
        Class.forName(nameConnection);
        
        tmpCon = "jdbc:" + "mysql://"+ip+":"+port+"/bd_airport";
        con = DriverManager.getConnection(tmpCon, login, motdepasse);
        instruc =  con.createStatement(TYPE_SCROLL_INSENSITIVE, CONCUR_READ_ONLY);
    }
    
    public void log(String msg) throws SQLException{
        int idLog=0;
        Date date = new Date();
        try{
            ResultSet rs = query("SELECT max(idLog) FROM log");
            rs.next();
            idLog = rs.getInt(1);
        }catch(Exception e){
            idLog =0;
        }
        String update = "INSERT INTO log(idLog, msg, date) VALUES("+idLog+","+msg+","+date+")";
        update(update);
    }
    
    public ResultSet query(String pquery) throws SQLException
    {
        return instruc.executeQuery(pquery);
    }
    
    public int update(String pupdate) throws SQLException
    {
        return instruc.executeUpdate(pupdate);
    }
    
    public boolean execute(String pupdate) throws SQLException
    {
        return instruc.execute(pupdate);
    }
    
    //<editor-fold defaultstate="collapsed" desc="Getter">
    public String getNameConnection() {
        return nameConnection;
    }
    
    public String getLogin() {
        return login;
    }
    
    public String getMotdepasse() {
        return motdepasse;
    }
    
    public Connection getCon() {
        return con;
    }
}
