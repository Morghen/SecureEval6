/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protocole;

import Libs.CryptoToolbox;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Date;
import java.util.Random;

/**
 *
 * @author Morghen
 */
public class TickmapDigest {
    
    private String login;
    private long saltTime;
    private double saltRandom;
    private byte[] hash;
    
    public TickmapDigest(String login, String mdp) throws NoSuchAlgorithmException, NoSuchProviderException, IOException
    {
        setLogin(login);
        setSaltTime(new Date().getTime());
        setSaltRandom(new Random().nextDouble());
        setHash(CryptoToolbox.GenerateSaltyDigest(getSaltTime(), getSaltRandom(), mdp));
    }

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the saltTime
     */
    public long getSaltTime() {
        return saltTime;
    }

    /**
     * @param saltTime the saltTime to set
     */
    public void setSaltTime(long saltTime) {
        this.saltTime = saltTime;
    }

    /**
     * @return the saltRandom
     */
    public double getSaltRandom() {
        return saltRandom;
    }

    /**
     * @param saltRandom the saltRandom to set
     */
    public void setSaltRandom(double saltRandom) {
        this.saltRandom = saltRandom;
    }

    /**
     * @return the hash
     */
    public byte[] getHash() {
        return hash;
    }

    /**
     * @param hash the hash to set
     */
    public void setHash(byte[] hash) {
        this.hash = hash;
    }

    

    
    
    
    
}
