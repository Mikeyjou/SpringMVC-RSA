/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.service;

import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.vo.EncryptionVo;
import java.security.PublicKey;
import java.util.Optional;

/**
 *
 * @author Mikey
 */
public interface IEncryptionService {
    void encryptionPersist(final Optional<EncryptionModel> encryption)
            throws Exception;
    
    Optional<EncryptionModel> encryptionFind(final EncryptionVo encryptionVo)
            throws Exception;
}
