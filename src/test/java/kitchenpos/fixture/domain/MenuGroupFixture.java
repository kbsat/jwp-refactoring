package kitchenpos.fixture.domain;

import kitchenpos.domain.MenuGroup;

public class MenuGroupFixture {

    public static MenuGroup 메뉴그룹A = createMenuGroup("메뉴그룹A");
    public static MenuGroup 메뉴그룹B = createMenuGroup("메뉴그룹B");
    public static MenuGroup 메뉴그룹C = createMenuGroup("메뉴그룹C");

    public static MenuGroup createMenuGroup(final String name) {
        return new MenuGroup(name);
    }
}
