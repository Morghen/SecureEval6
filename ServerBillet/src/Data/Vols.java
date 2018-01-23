/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Data;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.StringTokenizer;

/**
 *
 * @author 'Toine
 */
public class Vols implements Serializable{
    
    private int idVols;

    private String destination;

    private Date dateDepart;

    private Date dateArriver;

    private int nbrBillet;

    private int nbrDispo;

    public Vols() {
    }

    public Vols(int idVols, String destination, Date dateDepart, Date dateArriver, int nbrBillet, int nbrDispo) {
        this.idVols = idVols;
        this.destination = destination;
        this.dateDepart = dateDepart;
        this.dateArriver = dateArriver;
        this.nbrBillet = nbrBillet;
        this.nbrDispo = nbrDispo;
    }
    
    public Vols(String str) {
        StringTokenizer strTok = new StringTokenizer(str, "$");
        idVols = Integer.parseInt(strTok.nextToken());
        destination = strTok.nextToken();
        dateDepart= dateDepart;
        dateArriver=dateArriver;
        nbrBillet = Integer.parseInt(strTok.nextToken());
        nbrDispo = Integer.parseInt(strTok.nextToken());
    }

    public int getNbrDispo() {
        return nbrDispo;
    }

    public void setNbrDispo(int nbrDispo) {
        this.nbrDispo = nbrDispo;
    }

    public int getNbrBillet() {
        return nbrBillet;
    }

    public void setNbrBillet(int nbrBillet) {
        this.nbrBillet = nbrBillet;
    }

    public Date getDateArriver() {
        return dateArriver;
    }

    public void setDateArriver(Date dateArriver) {
        this.dateArriver = dateArriver;
    }

    public Date getDateDepart() {
        return dateDepart;
    }

    public void setDateDepart(Date dateDepart) {
        this.dateDepart = dateDepart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getIdVols() {
        return idVols;
    }

    public void setIdVols(int idVols) {
        this.idVols = idVols;
    }

}
