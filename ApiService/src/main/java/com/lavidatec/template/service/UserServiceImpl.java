/**
 * UPDUser_Service
 * Author : Sian
 * UPD User method
 */
package com.lavidatec.template.service;


import com.lavidatec.template.dao.IUsersDao;
import com.lavidatec.template.dao.UsersDaoImpl;
import com.lavidatec.template.entity.UsersModel;
import com.lavidatec.template.pojo.PasswordSHA3;
import com.lavidatec.template.vo.UsersVo;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;

/**
 * 2017/03/15 - Add Users_Model.
 */
@Lazy
@Repository("IUserService")
public class UserServiceImpl implements IUserService{

    /**
     * log init.
     */
    static final Logger LOGGER
            = LoggerFactory.getLogger(UserServiceImpl.class);
    
    private IUsersDao iUsersDao = new UsersDaoImpl();
//    private PasswordSHA3 sha = new PasswordSHA3();
    /**
     * Users Persist.
     *
     * @param users User Data
     * @throws Exception Exception
     */
    @Override
    public final void userPersist(final Optional<UsersModel> users)
            throws Exception {
        LOGGER.info("userPersistStart");
        
//        users.get().setPassword(sha.createHash(users.get().getPassword()));
        users.get().setPassword(users.get().getPassword());
        iUsersDao.usersPersist(users);
        LOGGER.info("userPersistEnd");
    }
    
    @Override
    public final void userMerge(final Optional<UsersModel> users)
            throws Exception {
        LOGGER.info("userMergeStart");
        iUsersDao.usersMerge(users);
        LOGGER.info("userMergeEnd");
    }
    
    @Override
    public final Optional<UsersModel> userFind(final Optional<UsersModel> users)
            throws Exception {
        LOGGER.info("userFindStart");
        UsersVo usersVo = new UsersVo();
        usersVo.setAccount(users.get().getAccount());
        if(StringUtils.isNotBlank(users.get().getPassword()))
//            usersVo.setPassword(sha.createHash(users.get().getPassword()));
            usersVo.setPassword(users.get().getPassword());
        if(StringUtils.isNotBlank(users.get().getIdentifier()))
            usersVo.setIdentifier(users.get().getIdentifier());
        
        if(users.get().getOptimisticLock() > 0){
            System.out.println("User Service optLock" + users.get().getOptimisticLock());
            usersVo.setOptimisticLock(users.get().getOptimisticLock());
        }
        Optional<UsersModel> result = iUsersDao.usersFind(usersVo);
        LOGGER.info("userFindEnd");
        return result;
    }
    
    @Override
    public final void userRemove(final Optional<UsersModel> users)
            throws Exception {
        LOGGER.info("userRemoveStart");
        UsersVo usersVo = new UsersVo();
        usersVo.setAccount(users.get().getAccount());
//        usersVo.setPassword(sha.createHash(users.get().getPassword()));
        usersVo.setPassword(users.get().getPassword());
        Optional<UsersModel> user = iUsersDao.usersFind(usersVo);
        //判斷使用者帳密
        if(user.isPresent()){
            iUsersDao.usersRemove(user);
        }
        LOGGER.info("userRemoveEnd");
    }
    
    //使用者登入
    @Override
    public final String userLogin(final Optional<UsersModel> users)
            throws Exception {
        String token = "";
        //判斷是否資料正確
        if(users.isPresent()){
            token = UUID.randomUUID().toString().replace("-", "");
            users.get().setIdentifier(token);
        }
        iUsersDao.usersMerge(users);
        return token;
    }
    
    //使用者登出
    @Override
    public final void userLogout(final Optional<UsersModel> users)
            throws Exception {
        UsersVo usersVo = new UsersVo();
        usersVo.setAccount(users.get().getAccount());
//        usersVo.setPassword(sha.createHash(users.get().getPassword()));
        usersVo.setPassword(users.get().getPassword());
        Optional<UsersModel> user = iUsersDao.usersFind(usersVo);
        if(user.isPresent()){
            user.get().setIdentifier("");
            iUsersDao.usersMerge(user);
        }
    }
}