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
<<<<<<< HEAD
import java.util.ArrayList;
=======
>>>>>>> 33f024ceb9d2139cb1a9335f96485249cca823be
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
<<<<<<< HEAD
    private static void buildReverseSBox() {
=======
    private static void buildReverseSBox(){
>>>>>>> 33f024ceb9d2139cb1a9335f96485249cca823be
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
<<<<<<< HEAD
    public static byte[][] getReverseSBox() {
        if (ReverseSBOX == null)
            buildReverseSBox();
=======
    public static byte[][] getReverseSBox(){
        if(ReverseSBOX == null){
            buildReverseSBox();
        }
>>>>>>> 33f024ceb9d2139cb1a9335f96485249cca823be
        return ReverseSBOX;
    }
    
    /* Atribut */
    private KeyGenerator keygen;
    private Feistel feistel;
    private byte[] key;
    
    /* Konstruktor */
    public BlockProcessor(byte[] key, int iterate){
        this.key = key;
        keygen = new KeyGenerator(key, iterate);
        feistel = new Feistel(iterate);
    }
    
    /* Proses cipherblok menghasilkan cipher */
    public byte[] process(byte[] plain) {
        /* Ubah plain menjadi matriks */
        byte [][] matriks = convertToMatrix(plain);
        
        /* Proses awal */
        matriks = diagonalTranspose(matriks);
        matriks = substitusiSBox(matriks);
        
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
        ArrayList<byte[][]> hasil = feistel.iterateProcess(left, right, keygen);
        
        /* Menggabung left & right */
        int row=0;
        for (byte[][] proces : hasil) {
            for (byte[] proce : proces) {
                matriks[row] = proce;
                row++;
            }
        }
        
        /* Proses akhir */
        matriks = diagonalTranspose(matriks);
        matriks = substitusiSBox(matriks);
        
        /* Ubah matriks menjadi array 1 dim */
        return convertToArray(matriks);
    }
    
    /* Dekrip dari cipher menghasilkan blok plain */
    public byte[] inversProcess(byte [] cipher) {
        /* Ubah plain menjadi matriks */
        byte [][] matriks = convertToMatrix(cipher);
        
        /* Proses awal */
        matriks = substitusiInversSBox(matriks);
        matriks = inversDiagonalTranspose(matriks);
        
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
        ArrayList<byte[][]> hasil = feistel.inversIterateProcess(left, right, keygen);
        
        /* Menggabung left & right */
        int row=0;
        for (byte[][] proces : hasil) {
            for (byte[] proce : proces) {
                matriks[row] = proce;
                row++;
            }
        }
        
        /* Proses akhir */
        matriks = inversDiagonalTranspose(matriks);
        matriks = substitusiInversSBox(matriks);
        
        /* Ubah matriks menjadi array 1 dim */
        return convertToArray(matriks);
    }
    
    /* Olah matriks */
    /*** Enkrip ***/
    public byte[][] diagonalTranspose(byte[][] matriks) {
        byte[][] ret = mirrorTransposeMatrix(matriks);
        ret = shiftMatrix(ret);
        return ret;
    }
    private byte[][] mirrorTransposeMatrix(byte[][] matriks) {
        byte[][] m = new byte[matriks.length][matriks[0].length];
        for(int i=0; i<matriks.length; i++)
            for(int j=0; j<matriks[i].length; j++)
                m[i][j] = matriks[j][matriks[i].length-i-1];
        
        return m;
    }
    // Shift kolom ke atas 
    private byte[][] shiftMatrix(byte[][] matriks) {
        byte[][] m = new byte[matriks.length][matriks.length];
        for(int i=0; i<matriks[0].length; i++) {
            for(int j=0; j<matriks.length; j++) {
                m[j][i] = matriks[(j+i+matriks.length) % matriks.length][i];
            }
        }
        return m;
    }
    /*** Dekrip ***/
    public byte[][] inversDiagonalTranspose(byte[][] matriks) {
        byte[][] ret = inversShiftMatrix(matriks);
        ret = inversMirrorTransposeMatrix(ret);
        return ret;
    }
    private byte[][] inversMirrorTransposeMatrix(byte[][] matriks) {
        byte[][] m = new byte[matriks.length][matriks[0].length];
        for(int i=0; i<matriks.length; i++)
            for(int j=0; j<matriks[i].length; j++)
                m[i][j] = matriks[matriks[i].length-j-1][i];
        
        return m;
    }
    // Shift kolom ke bawah
    private byte[][] inversShiftMatrix(byte[][] matriks) {
        byte[][] m = new byte[matriks.length][matriks.length];
        for(int i=0; i<matriks[0].length; i++) {
            for(int j=0; j<matriks.length; j++) {
                m[j][i] = matriks[(j-i+matriks.length) % matriks.length][i];
            }
        }
        return m;
    }
    
    /* Method substitusi SBOX */
    private byte[][] substitusiSBox(byte[][] blokPlain) {
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
    /* Substitusi dengan inversSBOX */
    private byte[][] substitusiInversSBox(byte[][] blokCipher) {
        byte [][] isbox = BlockProcessor.getReverseSBox();
        byte [][] ret = new byte[blokCipher.length][blokCipher[0].length];
        
        for(int i=0; i<blokCipher.length; i++) {
            for(int j=0; j<blokCipher[i].length; j++) {
                int col = blokCipher[i][j] & 0xF, row = (blokCipher[i][j] >> 4) & 0xF;
                ret[i][j] = isbox[row][col];
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
