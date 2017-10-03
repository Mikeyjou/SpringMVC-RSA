/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.pojo;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Suhmian
 */
public class AESEncryption {
     public static byte[] Encrypt(SecretKey secretKey, byte[] iv, String msg) throws Exception{
         Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); 
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));    
        System.out.println("AES_CBC_PKCS5PADDING IV:"+Base64.getEncoder().encodeToString(cipher.getIV()));
        System.out.println("AES_CBC_PKCS5PADDING Algoritm:"+cipher.getAlgorithm());
        byte[] byteCipherText = cipher.doFinal(msg.getBytes("UTF-8"));
        System.out.println("加密結果的Base64編碼：" + Base64.getEncoder().encodeToString(byteCipherText));

        return byteCipherText;
    }

    public static byte[] Decrypt(SecretKey secretKey, byte[] cipherText, byte[] iv) throws Exception{
        System.out.println(secretKey.getEncoded().length);
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING"); 
        cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));    
        byte[] decryptedText = cipher.doFinal(cipherText);
        String strDecryptedText = new String(decryptedText);
        System.out.println("解密結果：" + strDecryptedText);
        return decryptedText;
    }

    public String getRandomKey(){
        try{
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256,new SecureRandom( ) );
            SecretKey secretKey = keyGen.generateKey();
            return Base64.getEncoder().encodeToString(secretKey.getEncoded());
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    public String getRandomIV(){
        byte[] iv = new byte[128 / 8]; 
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(iv);
        return Base64.getEncoder().encodeToString(iv);
    }
    
    public static void main(String args[]) throws Exception{
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256,new SecureRandom( ) );
        SecretKey secretKey = keyGen.generateKey();
        String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Secret key: " + key);
        
        byte[] decodedKey = Base64.getDecoder().decode(key);
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, 32, "AES");
        System.out.println(originalKey.getEncoded().length);
        
        byte[] iv = new byte[128 / 8]; 
        SecureRandom prng = new SecureRandom();
        prng.nextBytes(iv);
        System.out.println(Base64.getEncoder().encodeToString(iv));
        System.out.println(Base64.getDecoder().decode(Base64.getEncoder().encodeToString(iv)).length);
        byte[] cipher = AESEncryption.Encrypt(secretKey, iv, "I am PlainText!!");
        AESEncryption.Decrypt(secretKey, cipher, iv);  
    }
}