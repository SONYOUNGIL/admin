package app.menu;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
  
  @PostMapping("/selectMenu")
  public Map<String, Object> selectMenu() {
    Result result = new Result();		
		try {			
			result.setData("dataList", menuService.selectMenu());
		} catch (Exception ex) {
			result.setFailMsg(ex, AppConstant.codeMap.get("EN001"));//"An error has occurred");			
		}		
		return result.getResult();
  }

  @PostMapping("/saveAllMenu")
  public Map<String, Object> saveAllMenu(@Valid @RequestBody List<Menu> list) {  
    list.forEach(x -> {
      log.info(x.toJson());
    });
    Result result = new Result();
		try {
			menuService.saveAllMenu(list);
      result.setMsg(result.STATUS_SUCESS, AppConstant.codeMap.get("IS001"));//"Saved successfully");
		} catch (Exception ex) {
			result.setFailMsg(ex, AppConstant.codeMap.get("EN002"));//"An error occurred while saving");
		}
		return result.getResult();
  }


  /**
   * Delete All
   * @param cbfList
   * @return
   */
  @PostMapping("/deleteAllMenu")
  public Map<String, Object> deleteAllMenu(@RequestBody List<Menu> list) {
    Result result = new Result();
		try {
			menuService.deleteAllMenu(list);
			result.setMsg(result.STATUS_SUCESS, AppConstant.codeMap.get("IS002"));//"Successfully deleted");
		} catch (Exception ex) {
      result.setFailMsg(ex, AppConstant.codeMap.get("EN003"));//"An error occurred while deleting");
		}
		return result.getResult();
  }
}