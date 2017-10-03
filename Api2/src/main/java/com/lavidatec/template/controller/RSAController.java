/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.controller;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.pojo.RSA;
import com.lavidatec.template.pojo.RSAEncryption;
import com.lavidatec.template.service.EncryptionServiceImpl;
import com.lavidatec.template.service.IEncryptionService;
import com.lavidatec.template.vo.EncryptionVo;
import java.security.interfaces.RSAPrivateKey;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.JsonObject;
/**
 *
 * @author Mikey
 */
@Controller
@RequestMapping("/RSA")
public class RSAController {
    
    /**
     * log init.
     */
    static final Logger LOGGER
            = LoggerFactory.getLogger(UsersController.class);
    
    IEncryptionService iEncryptionService = new EncryptionServiceImpl();
    Gson gson = new Gson();
    //取得public key
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Object> getRSAPublicKey()
            throws Exception{
        JSONObject result = new JSONObject();
        EncryptionVo encryptionVo = new EncryptionVo();
        encryptionVo.setId("api2");
        String publicKey = iEncryptionService.encryptionFind(encryptionVo).get().getPublicKey();
        result.put("publicKey", publicKey);
        JsonObject obj = new JsonObject();
        obj.addProperty("publicKey", publicKey);
        ApiResponse response = new ApiResponse(HttpStatus.OK,"Success", Arrays.asList(obj));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    //接收被加密的公私鑰，用自己的私鑰解開放入DB
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> encryptionPersist(@RequestParam("id") String id, 
                                                 @RequestParam("publicKey") String publicKey,
                                                 @RequestParam("privateKey") String privateKey)
            throws Exception{
        RSAEncryption encryption = new RSAEncryption();
        RSA rsa = new RSA();
        //取得私鑰來解密
        EncryptionVo encryptionVo = new EncryptionVo();
        encryptionVo.setId("api2");
        String priKey = iEncryptionService.encryptionFind(encryptionVo).get().getPrivateKey();
        System.out.println("Decryption:");
        byte[] publicDecryptResult = encryption.decryptByRSA(Base64.getDecoder().decode(publicKey), (RSAPrivateKey)rsa.convertString2Private(priKey));
        byte[] privateDecryptResult = encryption.decryptByRSA(Base64.getDecoder().decode(privateKey), (RSAPrivateKey)rsa.convertString2Private(priKey));
        System.out.println("Decrypted public key string: " + Base64.getEncoder().encodeToString(publicDecryptResult));
        System.out.println("Decrypted private key string: " + Base64.getEncoder().encodeToString(privateDecryptResult));
        EncryptionModel encryptionModel = new EncryptionModel();
        encryptionModel.setId(id);
        encryptionModel.setPublicKey(Base64.getEncoder().encodeToString(publicDecryptResult));
        encryptionModel.setPrivateKey(Base64.getEncoder().encodeToString(privateDecryptResult));
        
        iEncryptionService.encryptionPersist(Optional.of(encryptionModel));
        return null;
    }
}
