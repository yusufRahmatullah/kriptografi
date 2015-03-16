/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newcipherblock;

/**
 *
 * @author user
 */
public class BlockProcessor {
    
    /* SBOX */
    private static byte[][] SBOX = null;
    private static void buildSBox() {
        SBOX = new byte[16][16];
        int a=0;
        for(int i=0; i<16; i++)
            for(int j=0; j<16; j++) {
                SBOX[i][j] = (byte)(a&0xFF);
                a++;
            }
    }
    public static byte[][] getSBox() {
        if (SBOX == null)
            buildSBox();
        return SBOX;
    }
    
    /* Atribut */
    private byte[] key, plain;
    private byte[][] block;
    
    
}
