/**
 * User_Profile_Model
 * Author : Sian
 * Users_Model DB Mapping Object
 */
package com.lavidatec.template.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import lombok.Data;
import org.hibernate.annotations.ColumnTransformer;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.Type;

/**
 * .
 * 2017/03/15 - Add Users_Model
 */
@Data
@Entity
@Table(name = "Unit_Users")
@OptimisticLocking(type = OptimisticLockType.VERSION)
public class UsersModel implements Serializable {

    /**
     * .
     *
     */
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//  @GeneratedValue(generator = "nativeGenerator")
//  @GenericGenerator(name = "nativeGenerator", strategy = "sequence")
    @Column(name = "p_key", unique = true, nullable = false,
            precision = 20, scale = 0)
    private Long pKey;
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "Account", unique = true)
    private String account;
    @Column(name = "Password")
    private String password;
    @Column(name = "OrderList")
    private String orderList;
    @Column(name = "Identifier")
    private String identifier;
    @Column(name = "UpdateTime")
    private LocalDateTime updateTime;
    @Version
    @Column(name="OptimisticLock")
    private int optimisticLock;
    
    @Column(name = "RSA_Public")
    private String rsaPublic;
    @Column(name = "RSA_Privte")
    private String rsaPrivate;
    
//    @Column(name="aes_encrypt")
//    @ColumnTransformer(
//        forColumn = "aes_encrypt",
//        read="AES_DECRYPT(UNHEX(aes_encrypt), UNHEX(SHA2('secret', 512)))",
//        write="HEX(AES_ENCRYPT(?, UNHEX(SHA2('secret', 512))))" )
//    private String test;
    
//    @Type(type = "com.lavidatec.template.entity.JSONObjectUserType")
//    @Column(name = "vEmergencyContacts", columnDefinition = "JSON")
//    @Convert(converter = JpaConverterJson.class)
//    private UserJson userJson;
}
