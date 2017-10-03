/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.service;

import com.lavidatec.template.dao.EncryptionDaoImpl;
import com.lavidatec.template.dao.IEncryptionDao;
import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.pojo.RSA;
import static com.lavidatec.template.service.UserServiceImpl.LOGGER;
import com.lavidatec.template.vo.EncryptionVo;
import java.security.PublicKey;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mikey
 */
public class EncryptionServiceImpl implements IEncryptionService{
    
    /**
     * log init.
     */
    static final Logger LOGGER
            = LoggerFactory.getLogger(EncryptionServiceImpl.class);
    
    private IEncryptionDao iEncryptionDao = new EncryptionDaoImpl();
    
    @Override
    public final void encryptionPersist(final Optional<EncryptionModel> encryption)
            throws Exception {
        LOGGER.info("encryptionPersistStart");
        
        iEncryptionDao.encryptionPersist(encryption);
        LOGGER.info("encryptionPersistEnd");
    }
    
    @Override
    public final Optional<EncryptionModel> encryptionFind(final EncryptionVo encryptionVo)
            throws Exception {
        LOGGER.info("RSAPublicFindStart");
        Optional<EncryptionModel> result = iEncryptionDao.EncryptionFind(encryptionVo);
        LOGGER.info("RSAPublicFindEnd");
        return result;
    }
}
