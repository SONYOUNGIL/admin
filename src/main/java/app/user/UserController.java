package app.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

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
  
  /**
   * Rsa
   * @param request
   * @return
   * @throws Exception
   */
  @PostMapping("/getRsaKey")
  public Map<String, Object> getRsaKey(HttpServletRequest request) throws Exception {
      return rsaSecureEncoder.initRsa(request); // RSA 키 생성	
  }

  /**
   * init message
   * @return
   */
  @PostMapping("/getMsg")
  public Map<String, Object> getMsg() {
    Result result = new Result();
    result.setData("msg_code", AppConstant.codeMap);
    return result.getResult();
  }
  
  /**
   * login
   * @param user
   * @return
   */
  @PostMapping("/login")
  public Map<String, Object> login(HttpServletRequest request, @RequestBody Map<String, Object> paramMap) {
    Map<String, Object> map  = userService.login(request, paramMap);
		return map;
  }

  /**
   * 
   */
  @PostMapping("/selectUser")
  public Map<String, Object> selectUser(@RequestBody User user) {
    Result result = new Result();		
		try {			
			result.setData("dataList", userService.selectUser(user));
		} catch (Exception ex) {
			result.setFailMsg(ex, AppConstant.codeMap.get("EN001"));//"An error has occurred");			
		}		
		return result.getResult();
  }

  /**
   * 
   * @param user
   * @return
   */
  @PostMapping("/saveUser")
  public Map<String, Object> saveUser(@Valid @RequestBody User user) {  
    Result result = new Result();
		try {
			userService.saveUser(user);
			result.setMsg(result.STATUS_SUCESS, AppConstant.codeMap.get("IS001"));//"Saved successfully";
		} catch (Exception ex) {
			result.setFailMsg(ex, AppConstant.codeMap.get("EN002"));//"An error occurred while saving";
		}
		return result.getResult();
  }

  /**
   * 
   * @param user
   * @return
   */
  @PostMapping("/deleteUser")
  public Map<String, Object> deleteUser(@RequestBody User user) {
    Result result = new Result();
		try {
			userService.deleteUser(user);
			result.setMsg(result.STATUS_SUCESS, AppConstant.codeMap.get("IS002"));//"Successfully deleted");
		} catch (Exception ex) {
      result.setFailMsg(ex, AppConstant.codeMap.get("EN003"));//"An error occurred while deleting");
		}
		return result.getResult();
  }

  /**
   * Delete All
   * @param list
   * @return
   */
  @PostMapping("/deleteAllUser")
  public Map<String, Object> deleteAllUser(@RequestBody List<User> list) {
    Result result = new Result();
		try {
			userService.deleteAllUser(list);
			result.setMsg(result.STATUS_SUCESS, AppConstant.codeMap.get("IS002"));//"Successfully deleted");
		} catch (Exception ex) {
      result.setFailMsg(ex, AppConstant.codeMap.get("EN003"));//"An error occurred while deleting");
		}
		return result.getResult();
  }
}