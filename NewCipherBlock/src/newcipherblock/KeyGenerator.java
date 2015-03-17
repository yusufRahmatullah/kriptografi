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
public class KeyGenerator {
    /* Variabel-variabel statik */
    public static final int SHIFT_ROW = 0;
    public static final int SHIFT_COLUMN = 1;
    public static final int SHIFT_LEFT = 1;
    public static final int SHIFT_RIGHT = -1;
    public static final int SHIFT_UP = 1;
    public static final int SHIFT_DOWN = -1;
    
    /* Atribut */
    private byte[] key;
    private int size;
    private ArrayList<byte[][]> generatedKey;
    
    /* Constructor, setter, getter */
    public KeyGenerator(byte[] key, int size) {
        this.key = key;
        this.size = size;
        generateAllKeys();
    }
    public ArrayList<byte[][]> getGeneratedKey() {
        return generatedKey;
    }
    public void setKey(byte[] key) {
        this.key = key;
    }
    
    /* Proses utama */
    public void generateAllKeys() {
        generatedKey = new ArrayList<>();
        
        /* Ubah kunci jadi 2 dimensi */
        int dim = (int)Math.sqrt(key.length);
        byte[][] keyMatrix = new byte[dim][dim];
        int c=0;
        for(int i=0; i<dim; i++) {
            for(int j=0; j<dim; j++) {
                keyMatrix[i][j] = key[c];
                c++;
            }
        }
        
        /* Generate semua key, simpan ke generatedKey */
        for(int i=1; i<=size; i++) {
            keyMatrix = generate(keyMatrix);
            generatedKey.add(copyMatrix(keyMatrix));
        }
    }
    /* Generate key */
    public byte[][] generate(byte[][] keyMatrix) {
        /* Kunci lama */
        byte[][]  old, sbox;
        old = new byte[keyMatrix.length][keyMatrix[0].length];
        for(int i=0; i<old.length; i++)
            old[i] = Arrays.copyOf(keyMatrix[i], keyMatrix[i].length);
        System.out.println("\nKunci awal:");
        printHexMatrix(keyMatrix);
        
        /* Transpos diagonal */
        keyMatrix = diagonalTranspose(keyMatrix);
        System.out.println("\nDiagonal transpos:");
        printHexMatrix(keyMatrix);
        
        /* Shift kiri */
        keyMatrix = shiftKey(keyMatrix, SHIFT_ROW, SHIFT_LEFT);
        System.out.println("\nShift:");
        printHexMatrix(keyMatrix);
        
        /* Substitusi baris pertama */
        sbox = BlockProcessor.getSBox();
        for(int i=0; i<keyMatrix[0].length; i++) {
            int col = keyMatrix[0][i] & 0xF, row = (keyMatrix[0][i] >> 4) & 0xF;
            keyMatrix[0][i] = sbox[row][col];
        }
        System.out.println("\nSubstitusi");
        printHexMatrix(keyMatrix);
        
        /* Operasi XOR */
        for(int i=1; i<keyMatrix.length; i++)
            for(int j=0; j<keyMatrix[i].length; j++)
                keyMatrix[i][j] =(byte) (keyMatrix[i][j] ^ old[i][j] ^ keyMatrix[0][j]);
        System.out.println("\nXOR");
        printHexMatrix(keyMatrix);
        
        return keyMatrix;
    }
    
    /* Copy matriks */
    private byte[][] copyMatrix(byte[][] matriks) {
        byte[][] ret = new byte[matriks.length][matriks[0].length];
        for(int i=0; i<ret.length; i++) {
            ret[i] = Arrays.copyOf(matriks[i], matriks[i].length);
        }
        return ret;
    }
    
    
    /*** << Below is OK >> ***/
    
    /* Transpos diagonal */
    private byte[][] diagonalTranspose(byte[][] keyMatrix) {
        byte[][] ret = new byte[keyMatrix.length][keyMatrix[0].length];
        ret = transposeKey(keyMatrix);
        ret = shiftKey(ret, SHIFT_COLUMN, SHIFT_UP);
        return ret;
    }
    
    /* Transpos matriks */
    private byte[][] transposeKey(byte[][] keyMatrix) {
        byte[][] m = new byte[keyMatrix.length][keyMatrix[0].length];
        for(int i=0; i<keyMatrix.length; i++)
            for(int j=0; j<keyMatrix[0].length; j++)
                m[i][j] = keyMatrix[j][i];
        return m;
    }
    
    /* Method-method shift */
    private byte[][] shiftKey(byte[][] keyMatrix, int rk, int dir) {
        byte[][] ret = new byte[keyMatrix.length][keyMatrix[0].length];
        if (rk == SHIFT_ROW)
            ret = shiftRow(keyMatrix, dir);
        else if (rk == SHIFT_COLUMN)
            ret = shiftColumn(keyMatrix, dir);
        return ret;
    }
    private byte[][] shiftRow(byte[][] keyMatrix, int dir) {
        byte[][] m = new byte[keyMatrix.length][keyMatrix[0].length];
        for(int i=0; i<keyMatrix.length; i++) {
            for(int j=0; j<keyMatrix[i].length; j++) {
                m[i][j] = keyMatrix[i][ (j+dir*i+keyMatrix[i].length) % keyMatrix[i].length];
            }
        }
        return m;
    }
    private byte[][] shiftColumn(byte[][] keyMatrix, int dir) {
        byte[][] m = new byte[keyMatrix.length][keyMatrix[0].length];
        for(int i=0; i<keyMatrix[0].length; i++) {
            for(int j=0; j<keyMatrix.length; j++) {
                m[j][i] = keyMatrix[(j+dir*i+keyMatrix.length) % keyMatrix.length][i];
            }
        }
        return m;
    }
    
    /*** Buat debug ***/
    public void printIntMatrix(byte[][] keyMatrix) {
        for(byte[] a : keyMatrix) {
            for(byte b : a)
                System.out.print( (b&0xFF) + " ");
            System.out.println();
        }
    }
    public void printCharMatrix(byte[][] keyMatrix) {
        for(byte[] a : keyMatrix) {
            for(byte b : a)
                System.out.print( (char)b + " ");
            System.out.println();
        }
    }
    public void printHexMatrix(byte[][] keyMatrix) {
        for(byte[] a : keyMatrix) {
            for(byte b : a)
                System.out.printf( "%02x ", b);
            System.out.println();
        }
    }
    
}
