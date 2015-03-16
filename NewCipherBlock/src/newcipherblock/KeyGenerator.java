/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newcipherblock;

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
    private byte[][] keyMatrix;
    
    /* Constructor, setter, getter */
    public KeyGenerator(byte[] b) {
        key = b;
        int dim = (int)Math.sqrt(b.length);
        keyMatrix = new byte[dim][dim];
        int c=0;
        for(int i=0; i<dim; i++) {
            for(int j=0; j<dim; j++) {
                keyMatrix[i][j] = key[c];
                c++;
            }
        }
    }
    public byte[][] getKeyMatrix() {
        return keyMatrix;
    }
    public void setKey(byte[] key) {
        this.key = key;
    }
    
    /* Proses utama */
    public void generate() {
        /* Kunci lama */
        byte[][]  old, sbox;
        old = new byte[keyMatrix.length][keyMatrix[0].length];
        for(int i=0; i<old.length; i++)
            old[i] = Arrays.copyOf(keyMatrix[i], keyMatrix[i].length);
        
        /* Transpos diagonal & shift kiri */
        diagonalTranspose();
//        System.out.println("\nDiagonal transpos:");
//        printHexMatrix();
        shiftKey(SHIFT_ROW, SHIFT_LEFT);
//        System.out.println("\nShift:");
//        printHexMatrix();
        
        /* Substitusi baris pertama */
        sbox = BlockProcessor.getSBox();
        for(int i=0; i<keyMatrix[0].length; i++) {
            int col = keyMatrix[0][i] & 0xF, row = (keyMatrix[0][i] >> 4) & 0xF;
            keyMatrix[0][i] = sbox[row][col];
        }
//        System.out.println("\nSubstitusi");
//        printHexMatrix();
        
        /* Operasi XOR */
        for(int i=1; i<keyMatrix.length; i++)
            for(int j=0; j<keyMatrix[i].length; j++)
                keyMatrix[i][j] =(byte) (keyMatrix[i][j] ^ old[i][j] ^ keyMatrix[0][j]);
    }
    
    
    /*** << Below is OK >> ***/
    
    /* Transpos diagonal */
    private void diagonalTranspose() {
        transposeKey();
        shiftKey(SHIFT_COLUMN, SHIFT_UP);
    }
    
    /* Transpos matriks */
    private void transposeKey() {
        byte[][] m = new byte[getKeyMatrix().length][getKeyMatrix()[0].length];
        for(int i=0; i<getKeyMatrix().length; i++)
            for(int j=0; j<getKeyMatrix()[0].length; j++)
                m[i][j] = getKeyMatrix()[j][i];
        keyMatrix = m;
    }
    
    /* Method-method shift */
    private void shiftKey(int rk, int dir) {
        if (rk == SHIFT_ROW)
            shiftRow(dir);
        else if (rk == SHIFT_COLUMN)
            shiftColumn(dir);
    }
    private void shiftRow(int dir) {
        for(int i=1; i<keyMatrix.length; i++) {
            byte[] row = new byte[keyMatrix[i].length];
            for(int j=0; j<keyMatrix[i].length; j++) {
                row[j] = keyMatrix[i][ (j+dir*i+keyMatrix[i].length) % keyMatrix[i].length];
            }
            keyMatrix[i] = row;
        }
    }
    private void shiftColumn(int dir) {
        byte[][] m = new byte[keyMatrix.length][keyMatrix[0].length];
        for(int i=0; i<keyMatrix[0].length; i++) {
            for(int j=0; j<keyMatrix.length; j++) {
                m[j][i] = keyMatrix[(j+dir*i+keyMatrix.length) % keyMatrix.length][i];
            }
        }
        keyMatrix = m;
    }
    
    /** Buat debug **/
    public void printIntMatrix() {
        for(byte[] a : keyMatrix) {
            for(byte b : a)
                System.out.print( (b&0xFF) + " ");
            System.out.println();
        }
    }
    public void printCharMatrix() {
        for(byte[] a : keyMatrix) {
            for(byte b : a)
                System.out.print( (char)b + " ");
            System.out.println();
        }
    }
    public void printHexMatrix() {
        for(byte[] a : keyMatrix) {
            for(byte b : a)
                System.out.printf( "%02x ", b);
            System.out.println();
        }
    }
    
}
