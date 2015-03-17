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
public class Feistel {
    
    private int iterate;
    
    public Feistel(int iterate) {
        this.iterate = iterate;
    }
    
    /* Iterasi proses feistel 16 kali */
    public ArrayList<byte[][]> iterateProcess(byte[][]left, byte[][]right, KeyGenerator key) {
        ArrayList<byte[][]> process = new ArrayList<>();
        process.add(left);
        process.add(right);
        
        /* Iterasi */
        for(int i=1; i<=iterate; i++)
            process = feistelUnit(key.getGeneratedKey().get(i-1), process.get(0), process.get(1), i);
        
        return process;
    }
    
    
    public ArrayList<byte[][]> inversIterateProcess(byte[][]left, byte[][]right, KeyGenerator key) {
        ArrayList<byte[][]> process = new ArrayList<>();
        process.add(left);
        process.add(right);
        
        /* Iterasi */
        for(int i=iterate; i>=1; i--) {
            process = inversFeistelUnit(key.getGeneratedKey().get(i-1), process.get(0), process.get(1), i);
        }
        
        return process;
    }
    
    /* Proses yang diiterasi 
       Kembalian arraylist, index 0 = left untuk iterasi selanjutnya
                            index 1 = right untuk iterasi selanjutnya
    */
    public ArrayList<byte[][]> feistelUnit(byte[][]keyi, byte[][] left, byte[][]right, int iterate) {
        /* Fungsi F */
        byte[][] right1 = fFunction(keyi, right, iterate);
        
        /* Operasi XOR dengan left */
        for(int i=0; i<left.length; i++) {
            byte[] temp = new byte[left[i].length];
            for(int j=0; j<left[i].length; j++)
                temp[j] = (byte)(left[i][j] ^ right1[i][j]);
            right1[i] = temp;
        }
        
        /* return */
        ArrayList<byte[][]> ret = new ArrayList<>();
        ret.add(right);
        ret.add(right1);
        return ret;
    }
    
    public ArrayList<byte[][]> inversFeistelUnit(byte[][]keyi, byte[][]left, byte[][]right, int iterate) {
        /* Fungsi F */
        byte[][] left1 = fFunction(keyi, left, iterate);
        
        /* Operasi XOR dengan right */
        for(int i=0; i<right.length; i++) {
            byte[] temp = new byte[right[i].length];
            for(int j=0; j<right[i].length; j++)
                temp[j] = (byte)(right[i][j] ^ left1[i][j]);
            left1[i] = temp;
        }
        
        /* return */
        ArrayList<byte[][]> ret = new ArrayList<>();
        ret.add(left1);
        ret.add(left);
        return ret;
    }
    
    /* << OK >> */
    /* Fungsi F, 1 <= iterate <= 16 */
    public byte[][] fFunction(byte[][] keyi, byte[][] right, int iterate) {
        byte [][]ret = new byte[right.length][right[0].length];
        int shift = (iterate-1)%4;
        
//        System.out.println("Right:");
//        NewCipherBlock.printMatrixByte(right);
//        System.out.println("Key:");
//        NewCipherBlock.printMatrixByte(keyi);
        
        /* Operasi XOR */
        for(int i=0; i<right.length; i++)
            for(int j=0; j<right[i].length; j++)
                ret[i][j] = (byte) (right[i][j] ^ keyi[i][j] ^ keyi[i+2][j]);
        
//        System.out.println("Setelah XOR:");
//        NewCipherBlock.printMatrixByte(ret);
        
        
//        System.out.println("Awal:");
//        NewCipherBlock.printMatrixByte(ret);
        
        /* Shift kiri baris */
        for(int i=0; i<ret.length; i++){
            byte[] temp = new byte[ret[i].length];
            for(int j=0; j<temp.length; j++)
                temp[j] = ret[i][ (j+shift+ret[i].length) % ret[i].length];
            ret[i] = temp;
        }
        
//        System.out.println("Akhir:");
//        NewCipherBlock.printMatrixByte(ret);
        
        return ret;
    }
    
}