package com.cfecweb.leon.server;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;

import java.util.Base64;
import java.util.Base64.*;

public class LEONencrypt {

	private static final String UNICODE_FORMAT = "UTF8";
    public static final String DESEDE_ENCRYPTION_SCHEME = "DESede";
    private KeySpec myKeySpec;
    private SecretKeyFactory mySecretKeyFactory;
    private Cipher cipher;
    byte[] keyAsBytes;
    private String myEncryptionKey;
    private String myEncryptionScheme;
    SecretKey key;
 
    public LEONencrypt() {
        myEncryptionKey = "ts$CEFC#07-@notNOT*50wtf!!";
        myEncryptionScheme = DESEDE_ENCRYPTION_SCHEME;
        try {
			keyAsBytes = myEncryptionKey.getBytes(UNICODE_FORMAT);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			myKeySpec = new DESedeKeySpec(keyAsBytes);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			mySecretKeyFactory = SecretKeyFactory.getInstance(myEncryptionScheme);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			cipher = Cipher.getInstance(myEncryptionScheme);
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        try {
			key = mySecretKeyFactory.generateSecret(myKeySpec);
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    /**
     * Method To Encrypt The String
     */
    public String encrypt(String unencryptedString) {
        String encryptedString = null;
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] plainText = unencryptedString.getBytes(UNICODE_FORMAT);
            byte[] encryptedText = cipher.doFinal(plainText);
            encryptedString =Base64.getEncoder().encodeToString(encryptedText); 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedString;
    }
    /**
     * Method To Decrypt An Ecrypted String
     */
    public String decrypt(String encryptedString) {
        String decryptedText=null;
        try {
        	
            cipher.init(Cipher.DECRYPT_MODE, key);
            
            byte[] encryptedText = Base64.getDecoder().decode(encryptedString.getBytes());
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= bytes2String(plainText);
          
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
    /**
     * Returns String From An Array Of Bytes
     */
    private static String bytes2String(byte[] bytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            stringBuffer.append((char) bytes[i]);
        }
        return stringBuffer.toString();
    }
	
}
