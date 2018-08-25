/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Libs;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Arrays;

/**
 *
 * @author Morghen
 */
public class CryptoToolbox {
    
    public static byte[] GenerateSaltyDigest(long time, double rand, String mdp) throws NoSuchAlgorithmException, NoSuchProviderException, IOException {
        
        MessageDigest md = MessageDigest.getInstance("SHA-1", "BC");
        
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream bdos = new DataOutputStream(baos);
        bdos.writeLong(time);
        bdos.writeDouble(rand);
        
        md.update(baos.toByteArray());
        md.update(mdp.getBytes("UTF-8"));
        
        byte[] digest = md.digest();
        
        return digest;
    }
    
    public static boolean CheckHash(byte[] first, byte[] second) {
        
        return Arrays.equals(first, second);
    }
    
}
