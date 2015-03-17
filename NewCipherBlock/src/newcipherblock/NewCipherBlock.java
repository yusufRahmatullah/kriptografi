/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package newcipherblock;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

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
        //printMatrixByte(sbox);
        
        /* Key */
        byte key[]= new byte[16];
        byte a=0;
        for(int i=0; i<16;i++) {
            key[i] = a;
            a++;
        }
        
        /* Plain */
        byte plain[] = new byte[24];
        a=0;
        for(int i=0; i<24; i++) {
            plain[i] = (byte)((a%264) & 0xFF);
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
//        System.out.println("Plain awal :");
//        printArrayByte(plain);
//        BlockCipher bc = BlockCipher.getInstance();
//        byte[] cipher = bc.electronicCodeBook(plain, key);
//        System.out.println("\nCipher :");
//        printArrayByte(cipher);
//        byte[] plainLagi = bc.decryptECB(cipher, key);
//        System.out.println("\nPlain lagi :");
//        printArrayByte(plainLagi);
        byte[] konci = "namasayakhaidzir".getBytes();
        enkripFile(konci);
        dekripFile(konci);
    }
    
    public static void coba() {
        byte[] key = "NAMASAYAKHAIDZIR".getBytes();
        BlockCipher bc = BlockCipher.getInstance();
        File filePlain = new File("C:\\Users\\user\\Desktop\\plain.txt");
        File fileCipher = new File("C:\\Users\\user\\Desktop\\cipher.txt");
        File filePlain2 = new File("C:\\Users\\user\\Desktop\\plain2.txt");
        Path path = Paths.get(filePlain.getAbsolutePath());
        try {
            byte[] plain = Files.readAllBytes(path);
            byte[] cipher = bc.electronicCodeBook(plain, key);
            FileOutputStream fos = new FileOutputStream(fileCipher);
            fos.write(cipher);
            byte[] plain2 = bc.decryptECB(cipher, key);
            FileOutputStream fos2 = new FileOutputStream(filePlain2);
            fos2.write(plain2);
        } catch (IOException ex) {
            
        }
    }
    
    private static void enkripFile(byte[] key) {
        BlockCipher bc = BlockCipher.getInstance();
        File filePlain = new File("C:\\Users\\user\\Desktop\\coba.bmp");
        File fileCipher = new File("C:\\Users\\user\\Desktop\\cipher.bmp");
        Path path = Paths.get(filePlain.getAbsolutePath());
        try {
            byte[] plain = Files.readAllBytes(path);
            byte[] cipher = bc.electronicCodeBook(plain, key);
            FileOutputStream fos = new FileOutputStream(fileCipher);
            fos.write(cipher);
        } catch (IOException ex) {
            
        }
    }
    private static void dekripFile(byte[] key) {
        BlockCipher bc = BlockCipher.getInstance();
        File fileCipher = new File("C:\\Users\\user\\Desktop\\cipher.bmp");
        File filePlain2 = new File("C:\\Users\\user\\Desktop\\plain2.bmp");
        Path path = Paths.get(fileCipher.getAbsolutePath());
        try {
            byte[] cipher = Files.readAllBytes(path);
            byte[] plain2 = bc.decryptECB(cipher, key);
            FileOutputStream fos2 = new FileOutputStream(filePlain2);
            fos2.write(plain2);
        } catch (IOException ex) {
            
        }
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
