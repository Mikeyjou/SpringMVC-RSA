/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lavidatec.template.dao;

import com.lavidatec.template.comment.JPAUtil;
import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.vo.EncryptionVo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Mikey
 */
public class EncryptionDaoImpl implements IEncryptionDao{
    
    /**
     * log init.
     */
    static final Logger LOGGER
            = LoggerFactory.getLogger(EncryptionDaoImpl.class);
    
    @Override
    public final int encryptionPersist(Optional<EncryptionModel> encryptionOptional)
            throws Exception{
        LOGGER.info("Start encryptionPersist");

        //回傳結果預設值
        int result = 0;
        //建立連線物件
        EntityManager em
                = JPAUtil.getEntityManagerFactory().createEntityManager();

        //建立資料交換物件
        EntityTransaction et = null;
        try {

            //傳入物件是否為空
            if (encryptionOptional.isPresent()) {
                //轉換物件資訊
                EncryptionModel encryptionModel = encryptionOptional.orElse(new EncryptionModel());
                et = em.getTransaction();
                et.begin();
                //寫入物件
                LOGGER.debug("Start persist");
                em.persist(encryptionModel);
                et.commit();

                if (null != encryptionModel.getPKey()) {
                    result = 1;
                }
            }
        } catch (Exception e) {

            LOGGER.error("Persist error:" + e);
            //Persist error rollback
            if (et != null && et.isActive()) {
                et.rollback();
            }
        } finally {
            //關閉連線
            em.close();
        }

        LOGGER.info("End encryptionPersist");
        return result;
    }
    
    @Override
    public final Optional<EncryptionModel> EncryptionFind(
            EncryptionVo encryptionVo)
            throws Exception{
        LOGGER.info("Start EncryptionFind");

        //建立連線物件
        EntityManager em
                = JPAUtil.getEntityManagerFactory().createEntityManager();

        //建立搜尋字串
        StringBuilder sbSql = new StringBuilder();
        //建立回傳物件
        Optional<EncryptionModel> encryptionOptional
                = Optional.empty();
        try {

            EncryptionModel encryptionModel;

            sbSql.append("From EncryptionModel");
            sbSql.append(" Where 1=1");

            // 加入查詢參數
            List<String> criList = new ArrayList<>();
            Map<String, Object> paramMap = new HashMap<>();
            //判斷是否有加入條件
            if (StringUtils.isNotBlank(encryptionVo.getId())) {
                criList.add("id = :id");
                paramMap.put("id", encryptionVo.getId());
            }
            
            if (!criList.isEmpty()) {
                // 使用者輸入查詢條件
                criList.forEach((cri) -> {
                    sbSql.append(" AND ").append(cri).append(" ");
                });
            }

            LOGGER.info("SQL : " + sbSql);
            //建立查詢物件
            Query query = em.createQuery(
                    sbSql.toString(), EncryptionModel.class);

            //設定參數
            paramMap.keySet().forEach((parameterName) -> {
                query.setParameter(parameterName, paramMap.get(parameterName));
            });

            LOGGER.debug("Start Find");
            encryptionModel
                    = (EncryptionModel) query.getResultList().get(0);
            //取出查詢資訊
            encryptionOptional
                    = Optional.ofNullable(encryptionModel);
        } catch (Exception e) {
            LOGGER.error("Find error:" + e);
        } finally {
            //關閉連線
            em.close();
        }

        LOGGER.info("End EncryptionFind");
        return encryptionOptional;
    }
}
