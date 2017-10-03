/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 *
 * @author Mikey
 */
@Data
@Entity
@Table(name = "Unit_Encryption")
public class EncryptionModel implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "p_key", unique = true, nullable = false,
            precision = 20, scale = 0)
    private Long pKey;
    
    @Column(name = "id", unique = true)
    private String id;
    @Column(name = "privateKey", length=1024)
    private String privateKey;
    @Column(name = "publicKey", length=1024)
    private String publicKey;
    @Column(name = "AESKey", length=1024)
    private String AESKey;
    @Column(name = "AESIV", length=32)
    private String AESIV;
}
