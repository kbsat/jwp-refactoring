package kitchenpos.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.MenuRepository;
import kitchenpos.domain.Product;
import kitchenpos.dto.request.MenuProductRequest;
import kitchenpos.dto.request.MenuRequest;
import kitchenpos.dto.response.MenuProductResponse;
import kitchenpos.dto.response.MenuResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuGroupDao menuGroupDao;
    private final ProductDao productDao;

    public MenuService(
            final MenuRepository menuRepository,
            final MenuGroupDao menuGroupDao,
            final ProductDao productDao
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupDao = menuGroupDao;
        this.productDao = productDao;
    }

    @Transactional
    public MenuResponse create(final MenuRequest request) {
        validateMenuGroupExistById(request.getMenuGroupId());
        final Menu menu = new Menu(request.getName(), request.getPrice(), request.getMenuGroupId(),
                getMenuProducts(request));

        return toMenuResponse(menuRepository.save(menu));
    }

    private void validateMenuGroupExistById(final Long menuGroupId) {
        if (!menuGroupDao.existsById(menuGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    private List<MenuProduct> getMenuProducts(final MenuRequest request) {
        final List<MenuProductRequest> menuProductsRequest = request.getMenuProducts();

        return menuProductsRequest.stream()
                .map(it -> MenuProduct.
                        of(getProductById(it.getProductId()), it.getQuantity()))
                .collect(Collectors.toList());
    }

    private Product getProductById(final Long productId) {
        return productDao.findById(productId)
                .orElseThrow(IllegalArgumentException::new);
    }

    private static MenuResponse toMenuResponse(final Menu menu) {
        final List<MenuProductResponse> menuProductResponses = menu.getMenuProducts()
                .stream()
                .map(it -> new MenuProductResponse(it.getSeq(), it.getMenuId(), it.getProductId(), it.getQuantity()))
                .collect(Collectors.toList());

        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(),
                menu.getMenuGroupId(), menuProductResponses);
    }

    public List<MenuResponse> list() {
        final List<Menu> menus = menuRepository.findAll();

        return menus.stream()
                .map(MenuService::toMenuResponse)
                .collect(Collectors.toList());
    }
}
