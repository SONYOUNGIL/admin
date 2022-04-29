package app.menu;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MenuService {

    @Autowired
    private MenuRepository menuRepository;

    @Autowired
    private MenuMapper menuMapper;

    /**
     * Mybatis SELECT EXAMPLE
     * @param 
     * @return List<Menu>
     */
    public List<Map> selectMenu() {
        return menuMapper.selectMenu();
    }

    public void saveAllMenu(List<Menu> list) throws Exception {
        menuRepository.saveAll(list);
	}

    /**
     * Delete All Data
     * @param list
     * @return int
     * @throws Exception
     */
    public void deleteAllMenu(List<Menu> list) throws Exception {
        menuRepository.deleteAll(list);
	}
}
