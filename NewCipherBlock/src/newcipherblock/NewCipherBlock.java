/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newcipherblock;

import java.util.ArrayList;
import java.util.Arrays;

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
        byte[][] sbox = BlockProcessor.getSBox();
        System.out.println("SBOX: ");
//        printMatrixByte(sbox);
        
        /* Key */
        byte key[]= new byte[16];
        byte a=0;
        for(int i=0; i<16;i++) {
            key[i] = a;
            a++;
        }
        
        /* Plain */
        byte plain[] = new byte[16];
        a=0;
        for(int i=0; i<16;i++) {
            plain[i] = (byte)(0xF - a);
            a++;
        }
        
        /* Left & right */
        byte[][] left = new byte[2][4];
        byte[][] right = new byte[2][4];        
        int x = 0;
        for(int i=0; i<2; i++) {
            for(int j=0; j<4; j++) {
                left[i][j] = (byte)(x&0xFF);
                right[i][j] = (byte)(16-x);
                x++;
            }
        }
        
        /* Coba */
        BlockProcessor bp = new BlockProcessor();
        bp.process(plain, key);
    }
    
    public static void coba(int[] array) {
        for(int i=0; i<array.length; i++)
            array[i]++;
    }
    
    public static void printMatrixByte(byte[][] matriks) {
        for(byte[] ab : matriks) {
            for(byte b : ab)
                System.out.printf( "%02x ", b);
            System.out.println();
        }
        
    }
    
    public static void printArrayByte(byte[] array) {
        for(byte b : array) {
            System.out.printf("%02x ", b);
        }
        System.out.println();
    }
    
}
