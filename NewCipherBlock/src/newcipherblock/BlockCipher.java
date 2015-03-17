/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newcipherblock;

/**
 * class to encrypt using block cipher
 * there are 4 algorithm: ECB, CBC, CFB, and OFB
 * this class is build using singleton design pattern
 * @author Yusuf
 */
public class BlockCipher {
    
    private static BlockCipher instance = null;
    
    protected BlockCipher(){
        
    }
    
    public static BlockCipher getInstance(){
        if(instance == null){
            instance = new BlockCipher();
        }
        return instance;
    }
    
    public static byte[] electronicCodeBook(byte [] plain, byte[] key){
        byte[] retval = new byte[plain.length];
        int nBlock = (int)Math.ceil(plain.length/16);
        
        return retval;
    }
    
    public static byte[] cipherBlockChaining(byte [] plain, byte[] key){
        byte[] retval = new byte[plain.length];
        return retval;
    }
    
    public static byte[] cipherFeedback(byte [] plain, byte[] key){
        byte[] retval = new byte[plain.length];
        return retval;
    }
    
    public static byte[] outputFeedback(byte [] plain, byte[] key){
        byte[] retval = new byte[plain.length];
        return retval;
    }
}
