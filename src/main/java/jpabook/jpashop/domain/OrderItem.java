package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;
/*
    protected OrderItem() {
        //"createOrderItem 써라~"용도
    }
    >>@NoArgsConstructor(access = AccessLevel.PROTECTED)
    >>음? 다른 클래스에서 생성자 접근하는거 편집기 단에서 체크해주네?
    >>어노테이션(혹은 롬복)이 컴파일단(???, 애초에 편집기 백그라운드에서 체킹하는거 컴파일단 맞냐?)에서부터 작동하는 건가???
 */

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        //item에 price가 있잖아요? 왜 orderPrice 따로?
        //>> 할인 등의 변동사항 반영을 위한 처리
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }

    //==비지니스 로직==//
    public void cancel() {
        getItem().addStock(count); //getItem() ??? 롬복 @Getter로 생긴건가? >> 왜 item.getItem()이 아닐까 ???
    }

    //==조회 로직==//
    /**
     * 주문상품 전체 가격 조회
     */
    public int getTotalPrice() {
        return getOrderPrice() * getCount();
    }
}
