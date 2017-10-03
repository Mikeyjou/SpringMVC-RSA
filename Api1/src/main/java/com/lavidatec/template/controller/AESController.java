/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.controller;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;
import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.entity.UsersModel;
import com.lavidatec.template.pojo.AESEncryption;
import com.lavidatec.template.pojo.RSA;
import com.lavidatec.template.pojo.RSAEncryption;
import com.lavidatec.template.service.EncryptionServiceImpl;
import com.lavidatec.template.service.IEncryptionService;
import com.lavidatec.template.service.IUserService;
import com.lavidatec.template.service.UserServiceImpl;
import com.lavidatec.template.vo.EncryptionVo;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.Optional;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author Mikey
 */
@Controller
@RequestMapping("/AES")
public class AESController {
    
    /**
     * log init.
     */
    static final Logger LOGGER
            = LoggerFactory.getLogger(UsersController.class);
    
    IEncryptionService iEncryptionService = new EncryptionServiceImpl();
    IUserService iUserService = new UserServiceImpl();
    Gson gson = new Gson();
    //AES流程
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> saveAES(@RequestParam("account") String account,
                                           @RequestParam("password") String pwd,
                                           @RequestParam("data") String data)
            throws Exception{
        //init
        RSA rsa = new RSA();
        RSAEncryption rsaEncryption = new RSAEncryption();
        RestTemplate restTemplate = new RestTemplate();
        EncryptionVo encryptionVo = new EncryptionVo();
        encryptionVo.setId("api1");
        //取得公私鑰
        EncryptionModel encryptionModel = iEncryptionService.encryptionFind(encryptionVo).get();
        String pubKey = encryptionModel.getPublicKey();
        String priKey = encryptionModel.getPrivateKey();
        System.out.println("公: " + pubKey);
        System.out.println("私: " + priKey);
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestBody.add("id", "api1");
        //傳送公鑰過去
        requestBody.add("publicKey", pubKey);
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(requestBody ,
                headers);
        //取得被傳送過去的公鑰加密過的AES Key
        ResponseEntity response = restTemplate.postForEntity("http://localhost:8081/api2/AES", entity, JSONObject.class);
        JSONObject jsonObj = new JSONObject(response.getBody().toString());
        String AESKey = jsonObj.get("AESKey").toString();
        String AESIV = jsonObj.get("AESIV").toString();
        System.out.println(AESKey);
        System.out.println(AESIV);
        byte[] AESKeyDecryptResult = rsaEncryption.decryptByRSA(Base64.getDecoder().decode(AESKey), (RSAPrivateKey)rsa.convertString2Private(priKey));
        byte[] AESIVDecryptResult = rsaEncryption.decryptByRSA(Base64.getDecoder().decode(AESIV), (RSAPrivateKey)rsa.convertString2Private(priKey));
        //用別的方法轉string 會出現padding
        String decryptAESKey = new String(rsaEncryption.unpadZeros(AESKeyDecryptResult), "UTF-8");
        String decryptAESIV = new String(rsaEncryption.unpadZeros(AESIVDecryptResult), "UTF-8");
        
        decryptAESKey = decryptAESKey.trim().replaceAll(" +", "");
        decryptAESKey = decryptAESKey.replaceAll("\\s+","");
        decryptAESIV = decryptAESIV.trim().replaceAll(" +","");
        decryptAESIV = decryptAESIV.replaceAll("\\s+","");
        System.out.println("Decrypted AES key string: " + decryptAESKey);
        System.out.println("Decrypted AES iv string: " + decryptAESIV);
        
        UsersModel userModel = new UsersModel();
        userModel.setAccount(account);
        userModel.setPassword(pwd);
        userModel = iUserService.userFind(Optional.of(userModel)).get();
        
        byte[] decodedKey = Base64.getDecoder().decode(AESKey);
        // rebuild key using SecretKeySpec
        SecretKey originalKey = new SecretKeySpec(decodedKey, 0, 32, "AES");
        System.out.println(originalKey.getEncoded().length);
        String encryptData = Base64.getEncoder().encodeToString(AESEncryption.Encrypt(originalKey, Base64.getDecoder().decode(decryptAESIV), data));
        String decryptData = new String(AESEncryption.Decrypt(originalKey, Base64.getDecoder().decode(encryptData), Base64.getDecoder().decode(decryptAESIV)));
        userModel.setIdentifier(encryptData);
        System.out.println("Origin data: " + data);
        System.out.println("AES encrypt: " + encryptData);
        System.out.println("AES decrypt: " + decryptData);
        iUserService.userMerge(Optional.of(userModel));
        return null;
    }
}
