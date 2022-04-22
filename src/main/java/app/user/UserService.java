package app.user;

import java.security.PrivateKey;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import app.com.util.JwtManager;
import app.com.util.Result;
import app.com.util.RsaSecureEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    private JwtManager  jwtManager = new JwtManager();

    private RsaSecureEncoder rsaSecureEncoder = new RsaSecureEncoder();
    
    /**
     * Mybatis SELECT EXAMPLE
     * @param user
     * @return List<User>
     * @throws Exception
     */
    public Map<String, Object> selectUser(HttpServletRequest request, Map<String, Object> paramMap)  {
        Result result = new Result();
        HttpSession session = request.getSession();  
        try{
            System.out.println(paramMap);
            // log.info(rsaSecureEncoder.decryptRsa((PrivateKey) paramMap.get("privateKey"), (String) paramMap.get("userPwd")));
            paramMap.put("userId", rsaSecureEncoder.decryptRsa( (PrivateKey) session.getAttribute(rsaSecureEncoder.RSA_WEB_KEY), (String) paramMap.get("userId")));
            paramMap.put("userPwd", rsaSecureEncoder.decryptRsa( (PrivateKey) session.getAttribute(rsaSecureEncoder.RSA_WEB_KEY), (String) paramMap.get("userPwd")));

            User loginUser = userMapper.selectUser(paramMap);		
            if(!ObjectUtils.isEmpty(loginUser)) {            
                result.setData("token", jwtManager.generateJwtToken(loginUser));
                result.setMsg(result.STATUS_SUCESS, "Login success");
            }else {
                result.setMsg(result.STATUS_ERROR, "Password is incorrect.");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
		return result.getResult();
    }

    /**
     * Save Data
     * @param user
     * @throws Exception
     */
    public void saveUser(User user) throws Exception {
        userRepository.save(user);
	}

    /**
     * Save All Data
     * @param userList
     * @throws Exception
     */
    public void saveAllUser(List<User> userList) throws Exception {
        userRepository.saveAll(userList);
	}

    /**
     * Delete Data
     * @param user
     * @return int
     * @throws Exception
     */
    public void deleteUser(User user) throws Exception {
        userRepository.delete(user);
	}

    /**
     * Delete All Data
     * @param userList
     * @return int
     * @throws Exception
     */
    public void deleteAllUser(List<User> userList) throws Exception {
        userRepository.deleteAll(userList);
	}
}
