package com.lavidatec.template.pojo;

import com.lavidatec.template.pojo.RSAEncryption;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Calendar;
 
import javax.crypto.Cipher;
 
 
public class RSA {
    private static String ALGORITHM = "RSA/ECB/PKCS1Padding";
    
    public static void printKeyPair(KeyPair keyPair) {
        System.out.println("  public key: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
    System.out.println("  private key: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
    }
    
    public KeyPair getRandomKeyPair(){
        KeyPair keyPair = null;
        try{
            // Generate key pair, and load it into public key and private key instances.
            KeyPairGenerator keygen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = new SecureRandom();
//            random.setSeed("test".getBytes());
            keygen.initialize(1024, random); // TODO Change length may cause the result incorrect.
            keyPair = keygen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyPair;
    }
    
    public void encodeAndDecode(String input, PublicKey publicKey, PrivateKey privateKey){
        RSAEncryption encryption = new RSAEncryption();
        byte[] result = null;
        try {
            // Encrypt
            System.out.println("Encryption:");
            result = encryption.encryptByRSA(input.getBytes("UTF-8"), (RSAPublicKey)publicKey);

            // Decrypt
            System.out.println("Decryption:");
            byte[] decryptResult = encryption.decryptByRSA(result, (RSAPrivateKey)privateKey);
            System.out.println("Decrypted string: " + new String(decryptResult, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
    public PublicKey convertString2Public(String key){
        PublicKey result = null;
        try{
            byte[] publicBytes = Base64.getDecoder().decode(key.getBytes());
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            result = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public PrivateKey convertString2Private(String key){
        PrivateKey result = null;
        try{
            byte [] pkcs8EncodedBytes = Base64.getDecoder().decode(key.getBytes());
            
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            result = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return result;
    }
    
    public static void main(String[] args) {
        RSA rsa = new RSA();
        rsa.encodeAndDecode("jZsKb1ladR2swsd4suKK8X5WcpwiiRxfWT3mPVoq31U=", 
                rsa.convertString2Public("MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCauoklAXfNawLCrVlT2Ws0b1RiAj3MrASBYxMNh7pl8Zj45HRANO9Df4UliuZI97Ms/bZqlOtZWKin/559aggmKBfTA9wyVyMEFMTdxRgKgOBZjjpuKj1gkmcVQXAzDKbWikO7VFIEsJTjnRLUvJIUVZKNSaphQZkAHljfcq6hJQIDAQAB"),
                rsa.convertString2Private("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJq6iSUBd81rAsKtWVPZazRvVGICPcysBIFjEw2HumXxmPjkdEA070N/hSWK5kj3syz9tmqU61lYqKf/nn1qCCYoF9MD3DJXIwQUxN3FGAqA4FmOOm4qPWCSZxVBcDMMptaKQ7tUUgSwlOOdEtS8khRVko1JqmFBmQAeWN9yrqElAgMBAAECgYBsAN0OObIYbyPkJACAaPlOBpAOQaZFoaUbc76u6RkRY8z1nvIOh2Sj5vVXuVb5g/1L4GVejEijeyFu8xz8SfEZJRnaawx3qggNMYFcPe11NNbGzUBGyzu1dQ2rNcLyK/zcokg5O4IGx4GT4fQIpsI2yvfBeA6kvLOQgQI1mYeiAQJBAPZoAPnBNeXMm2CpchYdltDLmAaLwcu3l++YtxRohDeZdukc6moPnY0XLsHSHAvQYOKdzF0T3anAshoVQsjS8EECQQCgwMHLY+7f/E39evV/W2dlpx5s54pyxDuH+7TF/lC+vYZQjTG0/+tdxzhyObB4jjr6+8Hr3B010lf/NmEcb/flAkAJhv/yqDvTRNQFKsgUftRkAltAdYrqu5COXRNHILtsAu0MGmgd7bijye+u4tbexhHY8U0DlE8PzzmTtJq+dRmBAkBooQ2B9GlmIDIi6gUf/74sy7lh6NKUIGUe/RWMSRsFCxaBF2VjCa+IOkLjFelmjiyVb4eonHvmrCHaWuqaN8aBAkEAm5mv3hW6DGf1espRNgtAt02gAD0VHTPSymDXMC6xg5LbeFJcSVN0ZLmAchgehUx28jAuxywqe04lFZ3rF7Hsig=="));
    }
}