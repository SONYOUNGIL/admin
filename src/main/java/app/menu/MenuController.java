package app.menu;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import app.com.config.AppConstant;
import app.com.util.Result;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Validated
@RestController
@RequestMapping("/admin/menu")
public class MenuController {

  @Autowired
  private MenuService menuService;
  
  @PostMapping("/getMsg")
  public Map<String, Object> getMsg() {
    Result result = new Result();
    result.setData("msg_code", AppConstant.codeMap);
    return result.getResult();
  }
}