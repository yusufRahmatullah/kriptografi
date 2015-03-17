/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newcipherblock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author user
 */
public class BlockProcessor {
    
    /* SBOX */
    private static byte[][] SBOX = null;
    private static byte[][] ReverseSBOX = null;
    private static void buildSBox() {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("sbox.data"));
            byte [] temp = new byte[256];
            fis.read(temp, 0, 256);
            SBOX = new byte[16][16];
            for(int i=0; i<16; i++)
                for(int j=0; j<16; j++) {
                    SBOX[i][j] = temp[i*16+j];
                }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BlockProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BlockProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(BlockProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    private static void buildReverseSBox(){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File("rsbox.data"));
            byte [] temp = new byte[256];
            fis.read(temp, 0, 256);
            ReverseSBOX = new byte[16][16];
            for(int i=0; i<16; i++)
                for(int j=0; j<16; j++) {
                    ReverseSBOX[i][j] = temp[i*16+j];
                }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(BlockProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(BlockProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fis.close();
            } catch (IOException ex) {
                Logger.getLogger(BlockProcessor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    public static byte[][] getSBox() {
        if (SBOX == null)
            buildSBox();
        return SBOX;
    }
    public static byte[][] getReverseSBox(){
        if(ReverseSBOX == null){
            buildReverseSBox();
        }
        return ReverseSBOX;
    }
    
    /* Atribut */
    
    /* Konstruktor */
    public BlockProcessor(){
    }
    
    /* Proses cipherblokm menghasilkan cipher */
    public byte[] process(byte[] plain, byte[] key) {
        /* Ubah plain menjadi matriks */
        byte [][] matriks = convertToMatrix(plain);
        
        /* Proses awal */
        matriks = diagonalTranspose(matriks);
        matriks = substitusi(matriks);
        
        /* Buat left & right */
        byte [][] left = new byte[2][4];
        byte [][] right = new byte[2][4];
        for(int i=0; i<2; i++) {
            for(int j=0; j<4; j++) {
                left[i][j] = matriks[i][j];
                right[i][j] = matriks[i+2][j];
            }
        }
        
        /* Proses dalam feistel */
        Feistel feistel = new Feistel(1);
        KeyGenerator keygen = new KeyGenerator(key, 1);
        matriks = feistel.iterateProcess(left, right, keygen);
        
        /* Proses akhir */
        matriks = diagonalTranspose(matriks);
        matriks = substitusi(matriks);
        
        /* Ubah matriks menjadi array 1 dim */
        return convertToArray(matriks);
    }
    
    /* Transpos matriks */
    private byte[][] diagonalTranspose(byte[][] matriks) {
        byte[][] ret = mirrorTransposeMatrix(matriks);
        ret = shiftMatrix(ret);
        return ret;
    }
    private byte[][] mirrorTransposeMatrix(byte[][] matriks) {
        byte[][] m = new byte[matriks.length][matriks[0].length];
        for(int i=0; i<matriks.length; i++)
            for(int j=0; j<matriks[0].length; j++)
                m[i][j] = matriks[j][matriks[0].length-i-1];
        
        return m;
    }
    private byte[][] shiftMatrix(byte[][] matriks) {
        byte[][] m = new byte[matriks.length][matriks.length];
        for(int i=0; i<matriks[0].length; i++) {
            for(int j=0; j<matriks.length; j++) {
                m[j][i] = matriks[(j+i+matriks.length) % matriks.length][i];
            }
        }
        return m;
    }
    
    /* Method substitusi SBOX */
    private byte[][] substitusi(byte[][] blokPlain) {
        byte [][] sbox = BlockProcessor.getSBox();
        byte [][] ret = new byte[blokPlain.length][blokPlain[0].length];
        
        for(int i=0; i<blokPlain.length; i++) {
            for(int j=0; j<blokPlain[i].length; j++) {
                int col = blokPlain[i][j] & 0xF, row = (blokPlain[i][j] >> 4) & 0xF;
                ret[i][j] = sbox[row][col];
            }
        }
        return ret;
    }
    
    /* Mengubah array 1 dim menjadi 2 dim */
    private byte[][] convertToMatrix(byte[] arr) {
        int dim = (int)Math.sqrt(arr.length);
        byte [][] ret = new byte[dim][dim];
        int c=0;
        for(int i=0; i<dim; i++) {
            for(int j=0; j<dim; j++) {
                ret[i][j] = arr[c];
                c++;
            }
        }
        return ret;
    }
    
    /* Mengubah array 2 dim menjadi 1 dim */
    private byte[] convertToArray(byte[][] matriks) {
        byte[] ret = new byte[matriks.length*matriks[0].length];
        int a=0;
        for(int i=0; i<matriks.length; i++) {
            for(int j=0; j<matriks[i].length; j++) {
                ret[a] = matriks[i][j];
                a++;
            }
        }
        
        return ret;
    }
    
}
