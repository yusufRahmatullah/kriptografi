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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author user
 */
public class NewCipherBlock {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException {
        // TODO code application logic here
        long start, finish, ecb, cbc, cfb, ofb, aes;
        long decb, dcbc, dcfb, dofb, daes;
        
        //plainteks
        byte[] plain = "abcdefghijklmnopabcdefghijklmnopabcdefgh".getBytes();
        byte[] key = "kriptografiAES++".getBytes();
        
        javax.crypto.KeyGenerator keyGen = javax.crypto.KeyGenerator.getInstance("AES");
        keyGen.init(128);
        javax.crypto.SecretKey secKey = keyGen.generateKey();
        javax.crypto.Cipher eAES = javax.crypto.Cipher.getInstance("AES");
        eAES.init(javax.crypto.Cipher.ENCRYPT_MODE, secKey);
        
        start = System.currentTimeMillis();
        byte[] eaes = eAES.doFinal(plain);
        finish = System.currentTimeMillis();
        aes = finish-start;
        
        start = System.currentTimeMillis();
        byte[] eECB = BlockCipher.electronicCodeBook(plain, key);
        finish = System.currentTimeMillis();
        ecb = finish-start;
        
        start = System.currentTimeMillis();
        byte[] eCBC = BlockCipher.cipherBlockChaining(plain, key);
        finish = System.currentTimeMillis();
        cbc = finish-start;
        
        start = System.currentTimeMillis();
        byte[] eCFB = BlockCipher.cipherFeedback(plain, key);
        finish = System.currentTimeMillis();
        cfb = finish-start;
        
        start = System.currentTimeMillis();
        byte[] eOFB = BlockCipher.outputFeedback(plain, key);
        finish = System.currentTimeMillis();
        ofb = finish-start;
        
        eAES.init(javax.crypto.Cipher.DECRYPT_MODE, secKey);
        start = System.currentTimeMillis();
        byte[] dAES = eAES.doFinal(eaes);
        finish = System.currentTimeMillis();
        daes = finish-start;
        
        start = System.currentTimeMillis();
        byte[] dECB = BlockCipher.decryptECB(eECB, key);
        finish = System.currentTimeMillis();
        decb = finish-start;
        
        start = System.currentTimeMillis();
        byte[] dCBC = BlockCipher.decryptCBC(eCBC, key);
        finish = System.currentTimeMillis();
        dcbc = finish-start;
        
        start = System.currentTimeMillis();
        byte[] dCFB = BlockCipher.decryptCFB(eCFB, key);
        finish = System.currentTimeMillis();
        dcfb = finish-start;
        
        start = System.currentTimeMillis();
        byte[] dOFB = BlockCipher.decryptOFB(eOFB, key);
        finish = System.currentTimeMillis();
        dofb = finish-start;
        
        System.out.println("plain : "+bytesToString(plain));
        System.out.println("key : "+bytesToString(key));
        System.out.println("-------------------------------------------------");
        System.out.println("enkripsi menggunakan AES : "+bytesToString(eaes));
        printArrayByte(eaes);
        System.out.println("enkripsi menggunakan ECB : "+bytesToString(eECB));
        printArrayByte(eECB);
        System.out.println("waktu yang dibutuhkan : "+ecb+" ms\n");
        System.out.println("enkripsi menggunakan CBC : "+bytesToString(eCBC));
        printArrayByte(eCBC);
        System.out.println("waktu yang dibutuhkan : "+cbc+" ms\n");
        System.out.println("enkripsi menggunakan CFB : "+bytesToString(eCFB));
        printArrayByte(eCFB);
        System.out.println("waktu yang dibutuhkan : "+cfb+" ms\n");
        System.out.println("enkripsi menggunakan OFB : "+bytesToString(eOFB));
        printArrayByte(eOFB);
        System.out.println("waktu yang dibutuhkan : "+ofb+" ms\n");
        System.out.println("-------------------------------------------------");
        System.out.println("dekripsi AES : "+bytesToString(eaes));
        System.out.println("waktu yang dibutuhkan : "+daes+" ms\n");
        System.out.println("dekripsi ECB : "+bytesToString(dECB));
        System.out.println("waktu yang dibutuhkan : "+decb+" ms\n");
        System.out.println("dekripsi CBC : "+bytesToString(dCBC));
        System.out.println("waktu yang dibutuhkan : "+dcbc+" ms\n");
        System.out.println("dekripsi CFB : "+bytesToString(dCFB));
        System.out.println("waktu yang dibutuhkan : "+dcfb+" ms\n");
        System.out.println("dekripsi OFB : "+bytesToString(dOFB));
        System.out.println("waktu yang dibutuhkan : "+dofb+" ms\n");
        System.out.println("-------------------------------------------------");
        System.out.println("setelah dilakukan perubahan pada byte pertama : ");
        eaes[0]^=0xFF;
        eECB[0]^=0xFF;
        eCBC[0]^=0xFF;
        eCFB[0]^=0xFF;
        eOFB[0]^=0xFF;
        
        eAES.init(javax.crypto.Cipher.DECRYPT_MODE, secKey);
        dAES = eAES.doFinal(eaes);
        dECB = BlockCipher.decryptECB(eECB, key);
        dCBC = BlockCipher.decryptCBC(eCBC, key);
        dCFB = BlockCipher.decryptCFB(eCFB, key);
        dOFB = BlockCipher.decryptOFB(eOFB, key);
        
        System.out.println("dekripsi AES : "+bytesToString(dAES));
        printArrayByte(dAES);
        System.out.println("dekripsi ECB : "+bytesToString(dECB));
        printArrayByte(dECB);
        System.out.println("dekripsi CBC : "+bytesToString(dCBC));
        printArrayByte(dCBC);
        System.out.println("dekripsi CFB : "+bytesToString(dCFB));
        printArrayByte(dCFB);
        System.out.println("dekripsi OFB : "+bytesToString(dOFB));
        printArrayByte(dOFB);
        System.out.println("-------------------------------------------------");
                
        /* Key */
        /*
        byte key[]= new byte[16];
        byte a=0;
        for(int i=0; i<16;i++) {
            key[i] = a;
            a++;
        }
        */
        /* Plain */
        /*
        byte plain[] = new byte[32];
        a=0;
        for(int i=0; i<32; i++) {
            plain[i] = (byte)((a%264) & 0xFF);
            a++;
        }
        */
        /* Coba */
//        System.out.println("Plain awal :");
//        printArrayByte(plain);
//        BlockCipher bc = BlockCipher.getInstance();
//        byte[] cipher = bc.cipherBlockChaining(plain, key);
//        System.out.println("\nCipher :");
//        printArrayByte(cipher);
//        byte[] plainLagi = bc.decryptCBC(cipher, key);
//        System.out.println("\nPlain lagi :");
//        printArrayByte(plainLagi);
        
        /*
        byte[] konci = "namasayakhaidzir".getBytes();
        enkripFile(konci);
        dekripFile(konci);
                */
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
            byte[] cipher = bc.cipherBlockChaining(plain, key);
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
            byte[] plain2 = bc.decryptCBC(cipher, key);
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
 
    public static String bytesToString(byte[] input){
        StringBuilder sb = new StringBuilder();
        for(byte b : input){
            sb.append((char)b);
        }
        return sb.toString();
    }
}
