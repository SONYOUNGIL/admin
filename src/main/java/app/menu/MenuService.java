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
     * @return List<Cbf>
     */
    public List<Map> selectMenu() {
        return menuMapper.selectMenu();
    }
}
