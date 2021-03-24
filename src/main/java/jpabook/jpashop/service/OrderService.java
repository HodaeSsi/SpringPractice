package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        /*
        생성 방법에 대한 단일화*
        생성자 + setter 조합 > 생성자는 protected를, setter는 전용 비지니스 메서드를 통해 방지한다.
        "코드는 제약적으로 짜는게 좋다."
         */

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        //왜 order만 save(persist)?
        //원래는 하위의 orderItems도 각각(아니면 리스트 통째로???) persist 해줘야 되는데,
        //Order/orderItems에 cascade = CascadeType.ALL 속성
        //>> "Order을 persist할때 orderItems도 자동으로 persist 해줘"
        //*cascade는 남용하면 안 됨.
        //이번엔 orderitem의 주인이 오직 order이고 참조 또한 order만 함이(item이 참조하던가?) 명확하고,
        //영속성 컨텍스트 라이프 사이클 상 persist 타이밍도 일치해서 사용(잘모르면 걍 cascade쓰지마셈 ㅋㅋㄹㅃㅃ)
        //만약에 orderitem이 더 중요하고, 여러곳에서 참조하는 엔티티였다면 cascade X > 따로 repo만들어서 따로 persist
        //**영속성 컨텍스트 좀 공부하자 잘 모르것다(웹 서버가 1, 2, ...로 분산되어있으면 영속성 컨텍스트는 어케되는거???)
        //사실 저까진 몰라도 되지않을까 싶긴하지만 어차피 공부 해야됨(토비???)

        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
    }

    //주문 검색
/*    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAll(orderSearch);
    }*/
}
