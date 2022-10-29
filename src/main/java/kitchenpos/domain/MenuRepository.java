package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.JdbcTemplateMenuDao;
import kitchenpos.dao.JdbcTemplateMenuProductDao;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import org.springframework.stereotype.Repository;

@Repository
public class MenuRepository {

    private final MenuDao menuDao;
    private final MenuProductDao menuProductDao;

    public MenuRepository(final JdbcTemplateMenuDao menuDao, final JdbcTemplateMenuProductDao menuProductDao) {
        this.menuDao = menuDao;
        this.menuProductDao = menuProductDao;
    }

    public Menu save(final Menu entity) {
        final List<MenuProduct> menuProducts = entity.getMenuProducts();
        final Menu savedMenu = menuDao.save(entity);

        final List<MenuProduct> savedMenuProducts = getMenuProducts(menuProducts, savedMenu);
        savedMenu.setMenuProducts(savedMenuProducts);

        return savedMenu;
    }

    private List<MenuProduct> getMenuProducts(final List<MenuProduct> menuProducts, final Menu savedMenu) {
        final Long menuId = savedMenu.getId();
        final List<MenuProduct> savedMenuProducts = new ArrayList<>();
        for (final MenuProduct menuProduct : menuProducts) {
            menuProduct.setMenuId(menuId);
            savedMenuProducts.add(menuProductDao.save(menuProduct));
        }

        return savedMenuProducts;
    }

    public Optional<Menu> findById(final Long id) {
        return menuDao.findById(id);
    }

    public List<Menu> findAll() {
        final List<Menu> menus = menuDao.findAll();
        for (final Menu menu : menus) {
            menu.setMenuProducts(menuProductDao.findAllByMenuId(menu.getId()));
        }

        return menus;
    }

    public long countByIdIn(final List<Long> ids) {
        return menuDao.countByIdIn(ids);
    }
}
