# 키친포스

## 요구 사항

### 상품(Product)

- 상품은 등록 가능하다.
    - 상품의 가격이 올바르지 않으면 등록할 수 없다.
        - 상품의 가격은 0 원 이상이어야 한다.
- 상품의 목록 조회가 가능하다.

### 메뉴 그룹(MenuGroup)

- 메뉴 그룹은 생성 가능하다.
- 메뉴 그룹의 목록 조회가 가능하다.

### 메뉴(Menu)

- 메뉴는 등록 가능하다.
    - 메뉴에 가격이 올바르지 않으면 등록할 수 없다.
        - 메뉴에 가격은 0 원 이상이어야 한다.
        - 메뉴의 가격은 메뉴 상품들의 가격의 총합보다 클 수 없다.
    - 지정한 메뉴 그룹이 존재하지 않으면 등록할 수 없다.
- 메뉴는 목록 조회가 가능하다.

### 주문(Order)

- 주문은 등록 가능하다.
    - 주문 항목이 비어있으면 등록할 수 없다.
    - 주문 항목에 기재된 메뉴가 존재하지 않을 경우 등록할 수 없다.
    - 주문 테이블이 존재하지 않을 경우 등록할 수 없다.
    - 주문 테이블이 비어있을 경우 등록할 수 없다
- 주문은 목록 조회가 가능하다.
- 주문 상태는 변경 가능하다.
    - 주문이 존재하지 않을 경우 변경할 수 없다.
    - 주문이 계산 완료된 경우 변경할 수 없다.
    - 주문 상태가 잘못된 경우 변경할 수 없다.
        - 주문 상태는 `COOKING`, `MEAL`, `COMPLETION` 만 가능하다.

### 테이블(Table)

- 주문 테이블은 등록 가능하다.
- 주문 테이블은 빈 테이블로 변경할 수 있다.
    - 주문 테이블이 없는 경우 변경할 수 없다.
    - 단체 지정이 되어있는 경우 변경할 수 없다.
    - 주문 테이블에 남아있는 주문이 존재하고 그 상태가 `COOKING`, `MEAL` 일 경우 변경할 수 없다.
- 주문 테이블은 목록 조회가 가능하다.
- 손님 수를 변경할 수 있다.
    - 손님 수를 0명 미만으로 변경할 수 없다.
    - 빈 테이블은 변경할 수 없다.

### 단체 지정(TableGroup)

- 테이블을 단체로 지정할 수 있다.
    - 빈 테이블이 아닐 경우 단체로 지정할 수 없다.
    - 두 개 미만의 테이블인 경우 단체로 지정할 수 없다.
    - 입력한 주문 테이블이 존재하지 않을 경우 단체로 지정할 수 없다.
    - 입력한 주문 테이블이 이미 단체로 지정된 경우 단체로 지정할 수 없다.
- 테이블의 단체 지정을 해제할 수 있다.
    - 주문 테이블에 남아있는 주문이 존재하고 그 상태가 `COOKING`, `MEAL`인 경우 변경할 수 없다.
    - 단체지정이 해제되면 주문 테이블을 비운다.

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |

## 서비스 리팩토링

- [x] 현재 서비스는 도메인 객체를 이용하여 View에서 입력, 출력 용도로도 사용하고 있다. 이를 해결하기 위해서 DTO를 만들어 해결해보자.
    - [x] 메뉴그룹
    - [x] 메뉴
    - [x] 주문
    - [x] 상품
    - [x] 테이블
    - [x] 단체지정
- [x] 불필요한 setter를 제거한다.
- [x] 테스트 픽스쳐 패키지를 분리한다.
- [x] 레포지토리를 리팩토링한다.
    - [x] MenuRepository
    - [x] OrderRepository ( Order를 완전한 불변으로 만들기는 실패 )
    - [x] TableGroupRepository 제거 ( 라이프사이클 불일치, 억지로 끼워 맞추는 느낌이 들었음 )

## 고민 주절주절

### MenuProduct에 price가 필요한가에 대한 고민

```
- 정책 : **Menu에 설정한 가격은 MenuProduct의 price 총합보다 클 수 없다.**
- if ( Product의 가격을 내리게 된다면? )
    - Product의 가격이 바뀐다고 ( 그것도 내려간다면? ) 가정해보자
    - 정책을 완전 반영한다면 Product가 변경 시 Menu의 가격이 정책에 어긋났는지 확인할 필요가 있다.
    - 이를 위해 Product의 update 코드에서 Menu들의 가격을 체크해주어야함. 이 때 MenuProduct로 엮여있는 Menu들을 모두 조회해와야 한다.
    - 또 여기서 MenuProduct에 price 들이 영속화되어 존재한다면 정합성을 맞추기 위해 업데이트 시 해당 영속화되어있는 MenuProduct에 대한 price도 수정이
      불가피해진다.
    - 해피케이스는 Menu의 가격을 미리 낮추고 -> Product의 가격을 낮추는 방식이 된다.
- 여기서 나오는 정책관련 고민
    - 먼저 정책에 대해서 고민을 해결하고 가자.
    - 해당 정책이 등장한 배경이 궁금하다. 이게 만약 실무였다면 해당 정책의 기획 의도를 파악하려 할 것 같다.
    - 그럼해당 정책의 목적에 대한 생각을 해보자
        - 처음에는 단순히 바가지를 막기 위한 정책인줄 알았다.
        - Product의 목적은 뭘까?
            - Product의 존재 이유는 메뉴를 생성할 때 여러 상품들이 메뉴 내에 존재할 수 있기 때문에 이와 같은 정책이 들어온 것 같다.
            - 그리고 기본적인 상품의 가격을 미리 책정해놓음으로써 여러 메뉴 내에서 가격의 일관성을 유지할 수 있을 것이다.
            - 그렇다면 해당 정책의 목적은 뭘까?
            - 방금 이야기 했던 일관성 측면에서, 동일한 상품이 어떤 메뉴에 속해있는지에 따라 다르게 가격 책정이 가능하다면 메뉴들의 가격 일관성이
              깨진다. 이는 소비자가 납득하기 어려운 메뉴들이 생성되는 문제점이 있다.
        - 위 같은 문제를 해결하기 위해서 해당 정책이 등장하지 않았나 싶다.
    - Product의 한 번 가격이 결정되면 절대 변경될 수 없다는 정책이 존재하고 있을 수 있다. (productService에 update가 없는것으로 봐서는?)
    - 나는 Product의 가격이 변경되지 않는다는 정책 하에서 생각할 것이다. 그렇다면 위의 가정은 일단 불가능해져서 해결됐다.
- 다시 돌아와서 price에 대한 이야기를 해보자.
    - MenuProduct는 Menu를 생성할 때만 필요하다.
        - 현재는 그렇다. 하지만 개인적으로는 메뉴에 대한 변경 기능을 추후 추가될 가능성이 높다고 생각한다.
    - 그렇다면 결국 ProductDao로부터 product를 가져올 필요가 있다. (productId를 입력받기 때문에)
    - 그럼 애초에 MenuProduct 내에 productId 대신 product를 가지게 한다면?
        - 개인적으로는 Product와 MenuProduct의 라이프사이클이 다르다는 생각이다. MenuProduct는 어쩌면 메뉴가 생성되는 타이밍에 함께 생성되고 삭제도 함께 된다.
          하지만 Product의 경우 생성을 미리 하고 후에 Menu가 생성된다. 생성의 주기가 다를 뿐더러 주문 입장에서는 Menu가 무엇인지에 관심이 있지 Product까지는 관심이 없을 수 있다.
        - 그렇기 때문에 불필요한 의존관계를 간접참조로 뜯어놓는 것이 좋지 않을까 싶다.
    - 그럼 일단 product 는 오케이.. 그럼 price는?
    - price를 MenuProduct에 넣음으로써 생기는 장점은 무엇일까?
        - Menu가 외부 의존을 받지 않아도 내부의 MenuProduct 만으로 검증이 가능하다.
        - Menu의 정책이다보니 Menu에 대한 책임 관점에서 스스로가 책임을 다할 수 있다.
    - price를 넣음으로써 단점은?
        - 정규화가 깨져 중복된 데이터가 존재한다는 불편함 ( ProductId가 있는데 굳이 price가 있어야하나? 하는 마음 )
        - 영속화에 대한 고민 -> 하나의 검증을 위해서 영속화하는 것이 맞나? 생성할 때만 사용되는 필드인데 이를 계속 들고다녀야할 이유는?
- menuProduct를 가져올 때 항상 join을 쳐서 가져온다면? ( ✅ )
    - menuProduct의 price에 대한 영속화를 굳이 하지 않아도 된다.
```

### TableGroup과 orderTable

```
- 둘은 같은 애그리거트라고 생각했지만 라이프사이클이 다르다는 느낌이다.
- 처음에는 억지로 끼워맞춰서 TableGroupRepository를 만들었으나.. 이는 실패했다. ( 서로 비즈니스 규칙의 충돌이 발생했음.. )
- TableGroupDao와 OrderTableDao 를 활용해서 다시 리팩토링해보자.
```

## 의존성 리팩토링

- 메뉴의 이름과 가격이 변경되면 주문 항목도 함께 변경된다. 메뉴 정보가 변경되더라도 주문 항목이 변경되지 않게 구현한다.
    - [x] 주문이 들어올 때 주문 내용을 OrderLineItem 에 직접 삽입한다.
    - [x] Order 조회 시 MenuName, MenuPrice 반환한다.
- 클래스 간의 방향도 중요하고 패키지 간의 방향도 중요하다. 클래스 사이, 패키지 사이의 의존 관계는 단방향이 되도록 해야 한다.
- [x] 도메인 간의 의존관계를 그려본다.
    - 도메인 간의 직접적인 의존관계에 대한 사이클은 보이지 않음.
- [x] 서비스 레이어에서의 의존관계를 그려본다.
- [x] Order-Table 간의 의존성 싸이클을 해결한다.
    - 싸이클 정리
        - Table 에서 해당 테이블을 비우거나 단체 지정을 해제할 때 테이블에 진행중인 주문이 있는지를 검사하기 위해 의존관계가 필요하다. (to OrderRepository)
        - Order 에서 빈 테이블인지 확인하기 위해 Table에 대한 의존관계가 필요하다. (to OrderTableDao)
    - Table -> Order 의존관계 끊기 ( 이제.. 이벤트 리스너를 곁들인 )
        - 먼저 Table 에서 주문이 진행되고 있는지 상태를 저장하기 위한 ordered 필드를 추가한다. (DB에도 반영 - V4)
        - 이벤트를 활용하여 Order가 생성, 또는 Completion 으로 상태가 변경될 경우 각각 이벤트로 table의 ordered 필드도 변경을 적용한다.
        - 덕분에 OrderTableService 와 TableGroupService 에서 OrderRepository에 대한 의존을 제거할 수 있었다.
        - 하지만 이벤트 리스너에서 Order 에 위치한 Event들을 의존하기 때문에 다시 문제가 생긴다.
    - Table 에 Order 가 여러 개 존재할 수 있다
        - 그렇기 때문에 리스너에서 이벤트를 받아 ordered 필드를 변경할 경우 해당 테이블에 존재하는 모든 주문이 결재 완료가 된 상태여야한다.
        - 이를 막기 위해서 OrderRepository.findByOrderTableId 를 추가하고 해당 로직을 이용해서 모든 주문의 상태를 확인한 후 ordered 상태를 바꾼다. (ordered ->
          false)
    - billing 패키지 도입
        - Table과 Order 사이의 의존관계가 이벤트리스너를 써도 끊어지지않는다.
        - 패키지 분리를 해보자.
        - billing 패키지 도입으로 의존관계가 정리되었다.
    - 솔직히..
        - 솔직히 이벤트리스너 방법보다 Validator를 구현, 의존성 역전 을 이용하여 의존방향을 단방향으로 바꿀 수 있었을 것이다.
        - 하지만 학습을 위해서 이벤트 리스너를 도입해보았다. 도입하니 생각보다 신기하다.
        - 처음에는 AbstractAggerateRoot 를 상속받아 이벤트를 발행하려했으나 Spring Data 를 사용했을 때 save 메서드 발생 시 적용된다고 한다.
        - 따라서 생 JdbcTemplate을 쓰는 지금 코드에서는 사용이 불가능해서 Publisher 를 직접 사용했다.
        - 
