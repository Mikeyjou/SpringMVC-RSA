/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.vo;

import lombok.Data;

/**
 *
 * @author Mikey
 */
@Data
public class EncryptionVo {
    
    private String id;
    private String type;
    private String privateKey;
    private String publicKey;
    private String AESKey;
}
