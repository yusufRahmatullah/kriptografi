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
    public byte[][] iterateProcess(byte[][]left, byte[][]right, KeyGenerator key) {
        byte[][] ret = new byte[left.length+right.length][left[0].length];
        ArrayList<byte[][]> process = new ArrayList<>();
        process.add(left);
        process.add(right);
        
        /* Iterasi */
        for(int i=1; i<=iterate; i++)
            process = feistelUnit(key.getGeneratedKey().get(i-1), process.get(0), process.get(1), i);
        
        /* Menggabung left & right */
        int row=0;
        for (byte[][] proces : process) {
            for (byte[] proce : proces) {
                ret[row] = proce;
                row++;
            }
        }
        
        return ret;
    }
    
    /****** Belum Beres *******/
    public byte[][] inversIterateProcess(byte[][]left, byte[][]right, KeyGenerator key) {
        byte[][] ret = new byte[left.length+right.length][left[0].length];
        ArrayList<byte[][]> process = new ArrayList<>();
        process.add(left);
        process.add(right);
        
        /* Iterasi */
        for(int i=1; i<=16; i++) {
            process = feistelUnit(key.getGeneratedKey().get(i-1), process.get(0), process.get(1), i);
        }
        
        /* Menggabung left & right */
        int row=0;
        for (byte[][] proces : process) {
            for (byte[] proce : proces) {
                ret[row] = proce;
                row++;
            }
        }
        
        return ret;
    }
    
    /* Proses yang diiterasi 
       Kembalian arraylist, index 0 = left untuk iterasi selanjutnya
                            index 1 = right untuk iterasi selanjutnya
    */
    public ArrayList<byte[][]> feistelUnit(byte[][]keyi, byte[][] left, byte[][]right, int iterate) {
        ArrayList<byte[][]> ret = new ArrayList<>();
        ret.add(right);
        
        /* Fungsi F */
        byte[][] right1 = fFunction(keyi, right, iterate);
        
        /* Operasi XOR dengan left */
        for(int i=0; i<left.length; i++) {
            byte[] temp = new byte[left[i].length];
            for(int j=0; j<left[i].length; j++)
                temp[j] = (byte)(left[i][j] ^ right1[i][j]);
            right1[i] = temp;
        }
        
        ret.add(right1);
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
