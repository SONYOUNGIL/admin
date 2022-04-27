package app.user;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.com.config.AppConstant;
import app.com.util.Result;
import app.com.util.RsaSecureEncoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/user")
public class UserController {

  @Autowired
  private UserService userService;
  
  private RsaSecureEncoder rsaSecureEncoder = new RsaSecureEncoder();
  
  @PostMapping("/getRsaKey")
  public Map<String, Object> getRsaKey(HttpServletRequest request) throws Exception {
      return rsaSecureEncoder.initRsa(request); // RSA 키 생성	
  }

  @PostMapping("/getMsg")
  public Map<String, Object> getMsg() {
    Result result = new Result();
    result.setData("msg_code", AppConstant.codeMap);
    return result.getResult();
  }
  
  /**
   * Select Data
   * @param user
   * @return
   */
  @PostMapping("/login")
  public Map<String, Object> login(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {
    Map<String, Object> map  = userService.selectUser(request, paramMap);
		return map;
  }
}