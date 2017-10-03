/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.controller;

import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.pojo.RSA;
import com.lavidatec.template.pojo.RSAEncryption;
import com.lavidatec.template.service.EncryptionServiceImpl;
import com.lavidatec.template.service.IEncryptionService;
import com.lavidatec.template.vo.EncryptionVo;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Base64;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
    
    //AES流程
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject encryptAES(@RequestParam("publicKey") String publicKey)
            throws Exception{
        RSA rsa = new RSA();
        RSAEncryption encryption = new RSAEncryption();
        RestTemplate restTemplate = new RestTemplate();
        
        EncryptionVo encryptionVo = new EncryptionVo();
        encryptionVo.setId("api2");
        EncryptionModel encryptionModel = iEncryptionService.encryptionFind(encryptionVo).get();
        String AESKey = encryptionModel.getAESKey();
        String AESIV = encryptionModel.getAESIV();
        System.out.println("公: " + publicKey);
        System.out.println("AES Key: " + AESKey);
        System.out.println("AES IV: " + AESIV);
        JSONObject jsonObj = new JSONObject();
        rsa.encodeAndDecode(AESKey, (RSAPublicKey)rsa.convertString2Public(publicKey), (RSAPrivateKey)rsa.convertString2Private("MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJq6iSUBd81rAsKtWVPZazRvVGICPcysBIFjEw2HumXxmPjkdEA070N/hSWK5kj3syz9tmqU61lYqKf/nn1qCCYoF9MD3DJXIwQUxN3FGAqA4FmOOm4qPWCSZxVBcDMMptaKQ7tUUgSwlOOdEtS8khRVko1JqmFBmQAeWN9yrqElAgMBAAECgYBsAN0OObIYbyPkJACAaPlOBpAOQaZFoaUbc76u6RkRY8z1nvIOh2Sj5vVXuVb5g/1L4GVejEijeyFu8xz8SfEZJRnaawx3qggNMYFcPe11NNbGzUBGyzu1dQ2rNcLyK/zcokg5O4IGx4GT4fQIpsI2yvfBeA6kvLOQgQI1mYeiAQJBAPZoAPnBNeXMm2CpchYdltDLmAaLwcu3l++YtxRohDeZdukc6moPnY0XLsHSHAvQYOKdzF0T3anAshoVQsjS8EECQQCgwMHLY+7f/E39evV/W2dlpx5s54pyxDuH+7TF/lC+vYZQjTG0/+tdxzhyObB4jjr6+8Hr3B010lf/NmEcb/flAkAJhv/yqDvTRNQFKsgUftRkAltAdYrqu5COXRNHILtsAu0MGmgd7bijye+u4tbexhHY8U0DlE8PzzmTtJq+dRmBAkBooQ2B9GlmIDIi6gUf/74sy7lh6NKUIGUe/RWMSRsFCxaBF2VjCa+IOkLjFelmjiyVb4eonHvmrCHaWuqaN8aBAkEAm5mv3hW6DGf1espRNgtAt02gAD0VHTPSymDXMC6xg5LbeFJcSVN0ZLmAchgehUx28jAuxywqe04lFZ3rF7Hsig=="));
        byte[] AESKeyEncryptResult = encryption.encryptByRSA(AESKey.getBytes("UTF-8"), (RSAPublicKey)rsa.convertString2Public(publicKey));
        byte[] AESIVEncryptResult = encryption.encryptByRSA(AESIV.getBytes("UTF-8"), (RSAPublicKey)rsa.convertString2Public(publicKey));
        jsonObj.put("AESKey", Base64.getEncoder().encodeToString(AESKeyEncryptResult));
        jsonObj.put("AESIV", Base64.getEncoder().encodeToString(AESIVEncryptResult));
        System.out.println(Base64.getEncoder().encodeToString(AESKeyEncryptResult));
        System.out.println(Base64.getEncoder().encodeToString(AESIVEncryptResult));
        return jsonObj;
    }
}
