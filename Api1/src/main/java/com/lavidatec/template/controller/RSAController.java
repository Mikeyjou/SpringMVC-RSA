/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.pojo.RSA;
import com.lavidatec.template.pojo.RSAEncryption;
import com.lavidatec.template.service.EncryptionServiceImpl;
import com.lavidatec.template.service.IEncryptionService;
import java.net.URI;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;



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
    //RSA一般流程
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Object> saveRSA()
            throws Exception{
        RSA rsa = new RSA();
        RSAEncryption encryption = new RSAEncryption();
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity response = restTemplate.getForEntity(URI.create("http://localhost:8081/api2/RSA"), Object.class);

        String json = gson.toJson(response.getBody());
        
        JSONObject jsonObj = new JSONObject(json);
        System.out.println(jsonObj);
        JSONArray jsonArr = jsonObj.getJSONArray("payload");
        System.out.println(jsonArr);
        JSONObject innerObj = jsonArr.getJSONObject(0);
        String serverPublicKey = innerObj.get("publicKey").toString();
        jsonObj.put("publicKey", serverPublicKey);
        
        KeyPair keyPair = rsa.getRandomKeyPair();
        EncryptionModel encryptionModel = new EncryptionModel();
        encryptionModel.setPublicKey(Base64.getEncoder().encodeToString(encryption.encryptByRSA(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()).getBytes("UTF-8"), (RSAPublicKey)rsa.convertString2Public(serverPublicKey))));
        
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<String, String>();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        requestBody.add("id", "api1");
        //用公鑰加密來傳送
        requestBody.add("publicKey", Base64.getEncoder().encodeToString(encryption.encryptByRSA(keyPair.getPublic().getEncoded(), (RSAPublicKey)rsa.convertString2Public(serverPublicKey))));
        requestBody.add("privateKey", Base64.getEncoder().encodeToString(encryption.encryptByRSA(keyPair.getPrivate().getEncoded(), (RSAPublicKey)rsa.convertString2Public(serverPublicKey))));
        final HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<MultiValueMap<String, String>>(requestBody ,
                headers);
        System.out.println("Public key: " + Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        System.out.println("Private key: " + Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        
        restTemplate.postForEntity("http://localhost:8081/api2/RSA", entity, ApiResponse.class);
        return null;
    }
}
