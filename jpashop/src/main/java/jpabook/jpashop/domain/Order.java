package jpabook.jpashop.domain;

import static javax.persistence.FetchType.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders") // 테이블명을 orders -> SQL에서 ORDER BY의 ORDER와 헷갈림
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 제약하는 스타일로 코딩해야 유지보수를 원활하게 할 수 있다.
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    // 멤버와 관계 세팅: order 입장에서 다대일 -> 멤버 하나가 여러 주문을 할 수 있으니.
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 연관관계 메소드
    public void setMember(Member member) {
        this.member = member;
        //setMember할 때 order쪽에도 같이 묶어서 넣는다. Member와 Order는 연관관계니까 이렇게 하면
        // Order에 member를 수정하는 작업을 추가로  하지 않고도 한 번에 변경 가능.
        member.getOrders().add(this);
    }

    // Order와 OrderItem도 양방향 관계이니 두 엔티티에 한 번에 변경하도록 한다.
    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 마찬가지로 delivery와 order는 연관관계.
    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    // 생성 메소드 -> 주문 생성은 로직이 복잡. order/orderItem 등 여러 개 생성해야됨. & 연관관계도 복잡 -> 별도의 생성 메소드가 있는 게 좋다.
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        Arrays.stream(orderItems).forEach(order::addOrderItem); // ::은 왼쪽에 쓴 객체(여기서는 Order)
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    // 비즈니스 로직
    // 주문 취소
    public void cancel() {
        if (delivery.getStatus() == DeliverStatus.COMP) {
            throw new IllegalStateException("이미 배송 완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        // orderItem 돌면서 재고 수량 원복
        orderItems.stream().forEach(OrderItem::cancel);
    }

    // 조회 로직
    // 전체 주문 가격 조회
    public int getTotalPrice() {
        int totalPrice = 0;
        totalPrice = orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
        return totalPrice;
    }



}