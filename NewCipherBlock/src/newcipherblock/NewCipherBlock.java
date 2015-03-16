/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newcipherblock;

import java.util.ArrayList;

/**
 *
 * @author user
 */
public class NewCipherBlock {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        byte [] b = new byte[16];
        byte [] plain = new byte[16];
        for(int i=0; i<b.length; i++)
            b[i] = (byte)( (15-i) & 0xFF );
        for(int i=0; i<plain.length; i++)
            plain[i] = (byte)( i & 0xFF );
        
        byte [][] left = new byte[2][4];
        byte [][] right = new byte[2][4];
        for(int i=0; i<2; i++) {
            System.arraycopy(plain, i*4, right[i], 0, 4);
            System.arraycopy(plain, i*4+8, left[i], 0, 4);
        }
        
        KeyGenerator key = new KeyGenerator(b);
        Feistel feistel = new Feistel();
        
        /* Print */
        System.out.println("\nLeft right lama : ");
        for(byte la[] : left) {
            for(byte lb : la)
                System.out.print( (lb&0xFF) + " ");
            System.out.println();
        }
        System.out.println();
        for(byte ra[] : right) {
            for(byte rb : ra)
                System.out.print( (rb&0xFF) + " ");
            System.out.println();
        }
//        ArrayList<byte[][]> hasil = feistel.feistelUnit(key.getKeyMatrix(), left, right, 1);
        byte[][] hasil = feistel.fFunction(key.getKeyMatrix(), right, 2);
        
        /* Print */
        System.out.println("\nLeft right baru : ");
        for(byte la[] : hasil) {
            for(byte lb : la)
                System.out.print( (lb&0xFF) + " ");
            System.out.println();
        }
        System.out.println();
        for(byte la[] : left) {
            for(byte lb : la)
                System.out.print( (lb&0xFF) + " ");
            System.out.println();
        }
    }
    
}
