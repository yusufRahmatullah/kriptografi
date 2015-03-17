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
    
    /* Mode ECB */
    public static byte[] electronicCodeBook(byte [] plain, byte[] key) {
        int nBlock = (int)Math.ceil(((double)plain.length)/16);
        byte[] retval = new byte[nBlock*16];
        int offset = 0;
        BlockProcessor bp = new BlockProcessor(key, 16);
        byte[] input = new byte[16];
        while (offset < plain.length) {
            if (offset+15 < plain.length) {
                System.arraycopy(plain, offset, input, 0, 16);
            } else {
                int sisa = (plain.length-offset);
                System.arraycopy(plain, offset, input, 0, sisa);
                for(int i=sisa; i<16; i++)
                    input[i] = 0;
            }
            System.arraycopy(bp.process(input), 0, retval, offset, 16);
            offset += 16;
        }
        return retval;
    }
    public byte[] decryptECB(byte [] cipher, byte[] key) {
        byte[] retval = new byte[cipher.length];
        int offset = 0;
        BlockProcessor bp = new BlockProcessor(key, 16);
        byte[] input = new byte[16];
        while (offset < cipher.length) {
            System.arraycopy(cipher, offset, input, 0, 16);
            System.arraycopy(bp.inversProcess(input), 0, retval, offset, 16);
            offset += 16;
        }
        return retval;
    }
    
    public static byte[] cipherBlockChaining(byte [] plain, byte[] key) {
        int nBlock = (int)Math.ceil(((double)plain.length)/16);
        byte[] retval = new byte[nBlock*16];
        int offset = 0;
        BlockProcessor bp = new BlockProcessor(key, 16);
        
        /* IV */
        byte [] iv = new byte[16];
        for(int i=0; i<16; i++)
            iv[i] = 0;
        
        byte[] input = new byte[16];
        byte[] output = new byte[16];
        byte[] c = new byte[16];
        byte[] p = new byte[16];
        c = iv;
        
        while (offset < plain.length) {
            if (offset+15 < plain.length) {
                System.arraycopy(plain, offset, p, 0, 16);
            } else {
                int sisa = (plain.length-offset);
                System.arraycopy(plain, offset, p, 0, sisa);
                for(int i=sisa; i<16; i++)
                    p[i] = 0;
            }
            for(int i=0; i<16; i++)
                input[i] = (byte)( (p[i] ^ c[i]) & 0xFF );
            output = bp.process(input);
            System.arraycopy(output, 0, retval, offset, 16);
            offset += 16;
            c = output;
        }
        return retval;
    }
    public byte[] decryptCBC(byte[] cipher, byte[] key) {
        byte[] retval = new byte[cipher.length];
        int offset = 0;
        BlockProcessor bp = new BlockProcessor(key, 16);
        
        /* IV */
        byte [] iv = new byte[16];
        for(int i=0; i<16; i++)
            iv[i] = 0;
        
        byte[] input = new byte[16];
        byte[] output = new byte[16];
        byte[] c = new byte[16];
        byte[] p = new byte[16];
        c = iv;
        
        while (offset < cipher.length) {
            System.arraycopy(cipher, offset, input, 0, 16);
            p = bp.inversProcess(input);
            for(int i=0; i<16; i++)
                output[i] = (byte)( (p[i] ^ c[i]) & 0xFF );
            System.arraycopy(output, 0, retval, offset, 16);
            offset += 16;
            System.arraycopy(input, 0, c, 0, 16);
        }
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
