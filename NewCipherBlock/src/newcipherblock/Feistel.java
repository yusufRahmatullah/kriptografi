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
    
    /* Iterasi proses feistel 16 kali */
    public byte[][] iterateProcess(byte[][]left, byte[][]right, KeyGenerator key) {
        byte[][] ret = new byte[left.length+right.length][left[0].length];
        ArrayList<byte[][]> process = new ArrayList<>();
        process.add(left);
        process.add(right);
        for(int i=1; i<=16; i++) {
            key.generate();
            process = feistelUnit(key.getKeyMatrix(), process.get(0), process.get(1), i);
        }
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
        byte[][] right1 = fFunction(keyi, right, iterate);
        for(int i=0; i<left.length; i++) {
            byte[] temp = new byte[left[0].length];
            for(int j=0; j<left[0].length; j++)
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
        
        /* Operasi XOR */
        for(int i=0; i<right.length; i++)
            for(int j=0; j<right[i].length; j++)
                ret[i][j] = (byte) (right[i][j] ^ keyi[i][j] ^ keyi[i+2][j]);
        
        /* Shift kiri baris */
        for(int i=0; i<ret.length; i++){
            byte[] temp = new byte[ret[i].length];
            for(int j=0; j<temp.length; j++)
                temp[j] = ret[i][ (j-shift+ret[i].length) % ret[i].length];
            ret[i] = temp;
        }
        return ret;
    }
    
}
