
import com.lavidatec.template.dao.IUsersDao;
import com.lavidatec.template.dao.UsersDaoImpl;
import com.lavidatec.template.entity.EncryptionModel;
import com.lavidatec.template.entity.UsersModel;
import com.lavidatec.template.pojo.AESEncryption;
import com.lavidatec.template.pojo.RSA;
import com.lavidatec.template.service.EncryptionServiceImpl;
import com.lavidatec.template.service.IEncryptionService;
import com.lavidatec.template.vo.EncryptionVo;
import com.lavidatec.template.vo.UsersVo;
import java.security.KeyPair;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.HashMap;
import java.util.Map;


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Mikey
 */
public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello! World!");
        IUsersDao iUsersDao = new UsersDaoImpl();
        UsersModel userModel = new UsersModel();
        userModel.setAccount("Test for it.......");
        userModel.setPassword("中文一下uuuudu");
        UsersVo usersVo = new UsersVo();
        
        EncryptionModel encryptionModel = new EncryptionModel();
        IEncryptionService iEncryptionService = new EncryptionServiceImpl();
        RSA rsa = new RSA();
        AESEncryption aesEncryption = new AESEncryption();
        KeyPair keyPair= rsa.getRandomKeyPair();
        
        encryptionModel.setPrivateKey(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
        encryptionModel.setPublicKey(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        encryptionModel.setId("api1");
        encryptionModel.setAESKey(aesEncryption.getRandomKey());
        encryptionModel.setAESIV(aesEncryption.getRandomIV());
        iEncryptionService.encryptionPersist(Optional.of(encryptionModel));
//        EncryptionVo encryptionVo = new EncryptionVo();
//        encryptionVo.setId("api2");
//        EncryptionModel api2 = iEncryptionService.RSAPublicFind(encryptionVo).get();
//        encryptionVo.setId("api1");
//        EncryptionModel api1 = iEncryptionService.RSAPublicFind(encryptionVo).get();
//        System.out.println("api1 rsa public: " + api1.getPublicKey());
//        System.out.println("api2 rsa public: " + api2.getPublicKey());
//        rsa.encodeAndDecode(api1.getPublicKey(), rsa.convertString2Public(api2.getPublicKey()), rsa.convertString2Private(api2.getPrivateKey()));
        
//        UserJson userJson = new UserJson();
//        Map<String, Boolean> concern = new HashMap<>();
//        concern.put("c67386d5-ad51-43ab-95d4-1c81aef5f7ad", Boolean.FALSE);
//        Map<String, String> nickname = new HashMap<>();
//        nickname.put("19bb5cba-567e-4937-bd20-f9ce8563b3d2", "Sandy");
//        nickname.put("c67386d5-ad51-43ab-95d4-1c81aef5f7ad", "nuck");
//        userJson.setConcern(concern);
//        userJson.setNickname(nickname);
//        userModel.setUserJson(userJson);
//        iUsersDao.usersPersist(Optional.of(userModel));
//        usersVo.setAccount("mikey");
//        usersVo.setPassword("123");
//        Optional<UsersModel> user = iUsersDao.usersFind(usersVo);
//        System.out.println(user.get().getUserJson().getNickname());
//        System.out.println(user.get().getUserJson().getConcern());

//        TrainServiceImpl trainService = new TrainServiceImpl();
//        TrainsModel trainModel = new TrainsModel();
//        trainModel.setDate("2017/09/11");
//        trainModel.setNo("111");
//        trainModel.setType("普悠瑪");
//        trainModel.setPrice("843");
//        trainModel.setTicketsLimit(20);
//        trainService.trainPersist(Optional.of(trainModel));
//        TrainsModel trainModel2 = new TrainsModel();
//        trainModel2.setDate("2017/09/12");
//        trainModel2.setPrice("843");
//        trainModel2.setNo("113");
//        trainModel2.setType("自強");
//        trainModel2.setTicketsLimit(20);
//        trainService.trainPersist(Optional.of(trainModel2));
//        Optional<List<TrainsModel>> optional;
//        TrainsVo trainVo = new TrainsVo();
//        trainVo.setNo("111");
//        System.out.print(trainService.trainFindList(trainVo));
    }
}
