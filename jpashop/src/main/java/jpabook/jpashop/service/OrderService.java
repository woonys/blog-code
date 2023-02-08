package jpabook.jpashop.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        // 주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        // 주문 저장
        orderRepository.save(order); // order 하나만 저장해도 delivery & item이 같이 자동 저장 => cascade 범위가 All로 설정되어 있어서. cascade는 persist 범위를 정해준다.
        // 참고: 함부로 cascade 범위를 all로 설정하면 안됨. 지금 케이스는 order만 persist하면 delivery & item도 같이 persist되는데(객체의 저장 라이프 사이클이 동일하기 때문에),
        // 이런 경우가 아니라면 cascade를 all로 설정하면 안됨.
        return order.getId();
    }

    // 취소
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 취소
        order.cancel(); // JPA를 사용하면 엔티티를 변경했을 때 트랜잭션이 끝나는 시점에 변경 감지(dirty checking)가 되어 자동으로 update 쿼리가 날라간다.
        // mybatis를 사용하면 update 쿼리를 직접 날려줘야 하는 것과 달리 JPA는 위와 같은 장점이 있다!
    }

    public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }

    // 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }

    /**
     * 도메인 모델 패턴 vs. 트랜잭션 스크립트 패턴
     * SQL을 직접 다뤄야 하는 경우는 트랜잭션 스크립트 패턴을 주로 쓰게 되고,
     * JPA를 사용하면서 엔티티를 다루는 경우는 도메인 모델 패턴을 주로 쓰게 된다.
     * 무엇이 좋고 나쁘다기보다는 상황에 따라 다르다. 때로는 트랜잭션 스크립트가 더 유리할 수도 있음. 핵심은 유지보수.
     * 한 프로젝트 안에서도 트랜잭션 스크립트 패턴과 도메인 모델 패턴을 혼용해서 사용할 수도 있다.
     */
}
