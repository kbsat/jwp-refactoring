package kitchenpos.application;

import static kitchenpos.application.fixture.MenuFixture.createMenu;
import static kitchenpos.application.fixture.MenuGroupFixture.메뉴그룹A;
import static kitchenpos.application.fixture.OrderFixture.createOrder;
import static kitchenpos.application.fixture.OrderTableFixture.createOrderTable;
import static kitchenpos.application.fixture.OrderTableFixture.forUpdateEmpty;
import static kitchenpos.application.fixture.OrderTableFixture.forUpdateGuestNumber;
import static kitchenpos.application.fixture.ProductFixture.탕수육;
import static kitchenpos.application.fixture.TableGroupFixture.createTableGroup;
import static kitchenpos.application.fixture.dto.OrderTableDtoFixture.createOrderTableRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.request.OrderTableRequest;
import kitchenpos.dto.response.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class TableServiceTest extends ServiceTest {

    @Test
    @DisplayName("주문테이블을 등록한다.")
    void create() {
        // given
        final OrderTableRequest request = createOrderTableRequest(3, false);

        // when
        final OrderTableResponse actual = tableService.create(request);
        final Long actualId = actual.getId();

        // then
        assertAll(
                () -> assertThat(actualId).isNotNull(),
                () -> assertThat(orderTableDao.findById(actualId)).isPresent()
        );
    }

    @Test
    @DisplayName("주문테이블 목록을 조회한다.")
    void list() {
        // given
        테이블등록(createOrderTable(5, false));
        테이블등록(createOrderTable(5, false));

        // when
        final List<OrderTable> actual = tableService.list();

        // then
        assertThat(actual).hasSize(2);
    }

    @Test
    @DisplayName("테이블의 빈 상태로 변경한다. ( false -> true )")
    void changeEmpty_falseToTrue() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(5, false));

        // when
        final OrderTable updatedTable = tableService.changeEmpty(table.getId(), forUpdateEmpty(true));

        // then
        assertThat(updatedTable.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("테이블의 빈 상태로 변경한다. ( true -> false )")
    void changeEmpty_trueToFalse() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(5, true));

        // when
        final OrderTable updatedTable = tableService.changeEmpty(table.getId(), forUpdateEmpty(false));

        // then
        assertThat(updatedTable.isEmpty()).isFalse();
    }

    @Test
    @DisplayName("changeEmpty : 주문 테이블이 존재하지 않으면 예외가 발생한다.")
    void changeEmpty_noOrderTable_throwException() {
        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(999L, forUpdateEmpty(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("changeEmpty : 단체지정이 되어있으면 예외가 발생한다.")
    void changeEmpty_groupTable_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, true));
        final OrderTable table2 = 테이블등록(createOrderTable(3, true));
        단체지정(createTableGroup(table, table2));

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), forUpdateEmpty(false)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @ParameterizedTest
    @CsvSource(value = {"COOKING", "MEAL"})
    @DisplayName("changeEmpty : 남아있는 주문이 존재하면 예외가 발생한다.")
    void changeEmpty_existRemainOrder_throwException(final OrderStatus orderStatus) {
        // given
        final OrderTable table = 테이블등록(createOrderTable(3, false));
        final Menu menu = 메뉴등록(createMenu("탕수육_메뉴", 10_000, 메뉴그룹등록(메뉴그룹A), 상품등록(탕수육)));

        final Order order = 주문등록(createOrder(table, menu));
        주문상태변경(order, orderStatus);

        // when & then
        assertThatThrownBy(() -> tableService.changeEmpty(table.getId(), forUpdateEmpty(true)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("테이블의 손님수를 변경한다.")
    void changeNumberOfGuests() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(5, false));

        // when
        final OrderTable actual = tableService.changeNumberOfGuests(table.getId(), forUpdateGuestNumber(3));

        // then
        assertThat(actual.getNumberOfGuests()).isEqualTo(3);
    }

    @Test
    @DisplayName("changeNumberOfGuests ; 손님수를 0명 미만으로 변경하려는 경우 예외가 발생한다.")
    void changeNumberOfGuests_underZero_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(4, false));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), forUpdateGuestNumber(-1)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("changeNumberOfGuests : 빈 테이블에 손님수를 변경할경우 예외가 발생한다.")
    void changeNumberOfGuests_emptyTable_throwException() {
        // given
        final OrderTable table = 테이블등록(createOrderTable(4, true));

        // when & then
        assertThatThrownBy(() -> tableService.changeNumberOfGuests(table.getId(), forUpdateGuestNumber(2)))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
