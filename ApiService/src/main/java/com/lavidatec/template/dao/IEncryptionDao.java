/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.dao;

import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.vo.EncryptionVo;
import java.util.Optional;

/**
 *
 * @author Mikey
 */
public interface IEncryptionDao {
    
    int encryptionPersist(Optional<EncryptionModel> encryptionOptional)
            throws Exception;
    
    Optional<EncryptionModel> EncryptionFind(
            EncryptionVo encryptionVo)
            throws Exception;
}
